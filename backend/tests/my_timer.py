import time

class Timer:
    def __init__(self) -> None:
        self.time = 0

    def start(self):
        self.time = time.time()

    def measure_time(self):
        return time.time() - self.time