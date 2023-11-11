import asyncio
import websockets
import requests
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

async def connect_to_websocket_server(cookies, dashboard_id):
    async with websockets.connect(url, extra_headers={'Cookie': cookies}) as websocket:
        websocket.send(dashboard_id)
        message = await websocket.recv()
        return message
    
def main():
    pass


if __name__ == '__main__':
    main()
