import threading

class Counter:
    def __init__(self) -> None:
        self.lock = threading.Lock()
        self.count = 0

    def increase(self):
        with self.lock:
            self.count += 1
    
    def get_count(self):
        return self.count