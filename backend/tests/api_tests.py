import requests
from concurrent.futures import ThreadPoolExecutor, as_completed
import time

url = "http://localhost:8080"

def login(username, password):
    form_data = {'username': username, 'password': password}
    auth_response = requests.post(url + "/login", data=form_data)
    cookies = auth_response.cookies

    if auth_response.status_code != 200:
        raise Exception("LOGIN FAILED")
        
    return cookies

def login_user():
      return login("user@gmail.com", "testtest")

def generate_project_jsons(n):
    jsons = []
    for i in range(n):
        json = {"name": f"Project {i}", "createdBy": 2}
        jsons.append(json)

    return jsons

def post_project(json, cookies):
    start_time = time.time()
    response = requests.post(url + "/user/project", json=json, cookies=cookies)
    end_time = time.time()

    elapsed_time = end_time - start_time
    return response.status_code, elapsed_time       

def create_projects_test(cookies):
    jsons = generate_project_jsons(2000)

    with ThreadPoolExecutor(max_workers=100) as executor:
        futures = [executor.submit(post_project, json, cookies) for json in jsons]
        successful_responses = 0
        max_execution_time = 0
        total_execution_time = 0

        for future in as_completed(futures):
            try:
                status_code, elapsed_time = future.result()

                max_execution_time = max(max_execution_time, elapsed_time)
                total_execution_time += elapsed_time
                successful_responses += (status_code == 200)

            except Exception as e:
                print(f"Error: {e}")

    average_execution_time = total_execution_time / len(jsons)

    success_rate = successful_responses/len(jsons)

    return average_execution_time, max_execution_time, success_rate

def generate_topic_jsons(n, project_id):
    jsons = []

    for i in range(n):
        json = {"name": f"Topic {i}", "uniqueName": f"topic {i}", "projectId": project_id, "valueType": "FLOAT"}
        jsons.append(json)

    return jsons


def post_topic(json, cookies):
    start_time = time.time()
    response = requests.post(url + "/user/topic", json=json, cookies=cookies)
    end_time = time.time()

    elapsed_time = end_time - start_time
    return response.status_code, elapsed_time    

def create_topics_test(cookies):
    project_json = {"name": "Project 1", "createdBy": 2}
    project_id = requests.post(url + "/user/project", json=project_json, cookies=cookies).json()["id"]
    jsons = generate_topic_jsons(1000, project_id)

    with ThreadPoolExecutor(max_workers=100) as executor:
        futures = [executor.submit(post_topic, json, cookies) for json in jsons]
        successful_responses = 0
        max_execution_time = 0
        total_execution_time = 0

        for future in as_completed(futures):
            try:
                status_code, elapsed_time = future.result()

                max_execution_time = max(max_execution_time, elapsed_time)
                total_execution_time += elapsed_time
                successful_responses += (status_code == 200)

            except Exception as e:
                print(f"Error: {e}")

    average_execution_time = total_execution_time / len(jsons)

    success_rate = successful_responses/len(jsons)

    return average_execution_time, max_execution_time, success_rate

def main():
    cookies = login_user()
    average_execution_time, max_execution_time, success_rate = create_projects_test(cookies)
    print(average_execution_time, max_execution_time, success_rate)

    average_execution_time, max_execution_time, success_rate = create_topics_test(cookies)
    print(average_execution_time, max_execution_time, success_rate)

if __name__ == '__main__':
    main()