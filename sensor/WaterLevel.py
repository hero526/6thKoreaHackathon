import serial
from websocket import create_connection

global ser

alert_flag = 0

def alert_sending():
    global alert_flag
    level = int(ser.readline())
    print(level)
    if level > 100 and alert_flag == 0: # send alert to server
        print("alert")
        alert_flag = 1
        ws = create_connection("ws://52.78.134.52:9000/")
        ws.send("level_alert: 1")
        ws.close()
    elif level < 100 and alert_flag == 1:
        print("alert clear")
        alert_flag = 0
        ws = create_connection("ws://52.78.134.52:9000/")
        ws.send("level_alert: 0")
        ws.close()


if __name__ == "__main__":
    ser = serial.Serial("/dev/ttyACM0", 9600)
    while 1:
        alert_sending()
