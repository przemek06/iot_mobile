import requests
from utils import get_random_string

url = "http://localhost:8080"

def login(username, password):
        form_data = {'username': username, 'password': password}
        auth_response = requests.post(url + "/login", data=form_data)
        cookies = auth_response.cookies

        if auth_response.status_code != 200:
            raise Exception("LOGIN FAILED")
        
        return cookies

def login_admin():
      return login("admin@gmail.com", "testtest")

def login_user():
      return login("user@gmail.com", "testtest")

def test_data_validation(cookies):
      errors = 0
      cookies = login_user()

      # Wrong email format
      json_data = {"email": "wrong", "password": "12345678", "name": "James Doe"}
      wrong_email_response = requests.put(url + "/user/users", json=json_data, cookies=cookies)
      
      if wrong_email_response.status_code != 400:
            errors = errors + 1

      # Wrong password
      json_data = {"password": "1234"}
      wrong_password_response = requests.put(url + "/user/users/password", json=json_data, cookies=cookies)

      if wrong_password_response.status_code != 400:
            errors = errors + 1

      # Wrong name
      json_data = {"name": get_random_string(65), "password": "12345678", "email": "user@gmail.com"}
      wrong_name_response = requests.put(url + "/user/users", json=json_data, cookies=cookies)

      if wrong_name_response.status_code != 400:
            errors = errors + 1

      return errors

def test_data_transformation(cookies):
      errors = 0
      cookies = login_user()

      # Email update
      json_data = {"email": "wrong@gmail.com", "password": "12345678", "name": "James Doe"}
      update_email_response = requests.put(url + "/user/users", json=json_data, cookies=cookies)
      
      if update_email_response.status_code != 200:
            errors = errors + 1
      
      user = requests.get(url + "/user/users/info", cookies=cookies).json()
      if user["email"] != "wrong@gmail.com":
            errors = errors + 1


      # Password update
      json_data = {"password": "12345678"}
      update_password_response = requests.put(url + "/user/users/password", json=json_data, cookies=cookies)

      if update_password_response.status_code != 200:
            errors = errors + 1

      # Name update
      json_data = {"name": "Joe Joe", "password": "12345678", "email": "user@gmail.com"}
      update_name_response = requests.put(url + "/user/users", json=json_data, cookies=cookies)

      if update_name_response.status_code != 200:
            errors = errors + 1

      user = requests.get(url + "/user/users/info", cookies=cookies).json()
      if user["name"] != "Joe Joe":
            errors = errors + 1

      return errors

def test_data_error_handling(cookies):
      errors = 0
      cookies = login_user()

      # Wrong email format
      json_data = {"email": "wrong", "password": "12345678", "name": "Joe Joe"}
      wrong_email_response = requests.put(url + "/user/users", json=json_data, cookies=cookies)

      user = requests.get(url + "/user/users/info", cookies=cookies).json()

      if user["email"] != "user@gmail.com" or user["name"] != "James Doe":
            errors = errors + 1

      # Wrong name
      json_data = {"name": get_random_string(65), "password": "12345678", "email": "wrong@gmail.com"}
      wrong_name_response = requests.put(url + "/user/users", json=json_data, cookies=cookies)

      user = requests.get(url + "/user/users/info", cookies=cookies).json()

      if user["email"] != "user@gmail.com" or user["name"] != "James Doe":
            errors = errors + 1

      return errors

def update_to_default(cookies):
      json_data = {"email": "user@gmail.com", "password": "12345678", "name": "James Doe"}
      requests.put(url + "/user/users", json=json_data, cookies=cookies)
      json_data = {"password": "testtest"}
      requests.put(url + "/user/users/password", json=json_data, cookies=cookies)

def main():
      cookies = login_user()

      data_validation_errors = test_data_validation(cookies)
      print(data_validation_errors)
      update_to_default(cookies)
      data_transformation_errors = test_data_transformation(cookies)
      print(data_transformation_errors)
      update_to_default(cookies)
      data_error_handling_errors = test_data_error_handling(cookies)
      print(data_error_handling_errors)
      update_to_default(cookies)

if __name__ == "__main__":
      main()