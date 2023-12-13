import asyncio
import websockets
import requests
import threading
from my_timer import Timer
from counter import Counter
import time 
from concurrent.futures import ThreadPoolExecutor, as_completed


url = "http://localhost:8080"
websocket_url = "ws://localhost:8080"
times = {}
messages = {}
connections_number = 50
request_number = 10
sleep_time = 0.2
timer = Timer() 
counter = Counter()

def login(username, password):
    form_data = {'username': username, 'password': password}
    auth_response = requests.post(url + "/login", data=form_data)
    cookies = auth_response.cookies

    if auth_response.status_code != 200:
        raise Exception("LOGIN FAILED")
        
    return cookies

def login_user():
      return login("user@gmail.com", "testtest")

async def connect_to_websocket_server(cookies, dashboard_id, i):
    times[i] = 0
    messages[i] = []
    async with websockets.connect(websocket_url + "/components", extra_headers={'Cookie': f"JSESSIONID={cookies.get('JSESSIONID')}"}) as websocket:
        await websocket.send(str(dashboard_id))
        counter.increase()
        while True:
            message = await websocket.recv()
            messages[i].append(message) 
            times[i] = timer.measure_time()
    
def post_component(dashboard_id, topic, cookies):
    json = {
    "dashboardId": dashboard_id,
    "components": [
        {
            "componentType": "INPUT",
            "name": "testtest",
            "type": "test",
            "size": 10,
            "index": 1,
            "topic": {
                "id": topic["id"],
                "projectId": topic["projectId"],
                "name": topic["name"],
                "uniqueName": topic["uniqueName"],
                "valueType": topic["valueType"] 
                }
            }           
        ]
    }
    return requests.put(url + "/user/component", json=json, cookies=cookies).json()

def post_component_when_ready(dashboard_id, topic, cookies):
    while (counter.get_count() < connections_number):
        time.sleep(1)
    
    timer.start()
    with ThreadPoolExecutor(max_workers=100) as executor:
        for i in range(request_number):
            executor.submit(post_component, dashboard_id, topic, cookies)
            time.sleep(sleep_time)

def create_project(cookies):
    project_json = {"name": "Project 1", "createdBy": 2}
    return requests.post(url + "/user/project", json=project_json, cookies=cookies).json()["id"]

def create_dashboard(project_id, cookies):
    dashboard_json = {"name": "Dashboard 1",  "projectId": project_id}
    return requests.post(url + "/user/dashboard", json=dashboard_json, cookies=cookies).json()["id"]

def create_topic(project_id, cookies):
    topic_json = {"name": "Topicc 1", "uniqueName": "topicc 1", "projectId": project_id, "valueType": "FLOAT"}
    return requests.post(url + "/user/topic", json=topic_json, cookies=cookies).json()

def measure_time():
    total_length = sum(len(lst) for lst in messages.values())

    while total_length < request_number * connections_number:
        time.sleep(1)
        total_length = sum(len(lst) for lst in messages.values())
        print(total_length)

    average_time = sum(times.values()) / len(times)
    max_time = max(times.values())
    unique_sets = set(tuple(value) for value in messages.values())
    print(f"Average time: {average_time}")
    print(f"Max time: {max_time}")
    print(f"Number of different orders: {len(unique_sets)}")

async def handler(cookies, dashboard_id):
    await asyncio.wait([connect_to_websocket_server(cookies, dashboard_id, i) for i in range(connections_number)])

async def test_component_change_synchronization():
    cookies = login_user()

    project_id = create_project(cookies)
    dashboard_id = create_dashboard(project_id, cookies)
    topic = create_topic(project_id, cookies)
    
    component_post_thread = threading.Thread(target=post_component_when_ready, args = (dashboard_id, topic, cookies))
    component_post_thread.start()

    measuring_thread = threading.Thread(target=measure_time)
    measuring_thread.start()

    h = handler(cookies, dashboard_id)

    await asyncio.gather(h)
    
def main():
    asyncio.run(test_component_change_synchronization())


if __name__ == '__main__':
    main()
