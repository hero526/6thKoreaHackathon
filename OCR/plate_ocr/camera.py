# -*- coding: utf-8 -*-
from websocket import create_connection

from picamera import PiCamera
from ocr import Recognition

camera = PiCamera()
camera.framerate = 15

camera.start_preview(fullscreen=False, window=(0,0,600,500))
cnt = 1
recogtest = Recognition()

ws = create_connection("ws://52.78.134.52:9000/")
ws.send("carrier_num: 14다 7575")

while True:
	x = ws.recv()

	if x == "s":
		camera.stop_preview()
		break
	elif x == "capture":
		path = 'image'+str(cnt)+'.png'
		camera.capture(path)

		result=recogtest.ExtractNumber(path)
		cnt += 1
		if result != "Failed":
			ws.send("follower_num: "+result)
			print(result)
			ws.close()
			break
		else:
			print(x)

