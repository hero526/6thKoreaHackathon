from picamera import PiCamera


camera = PiCamera()

camera.start_preview(fullscreen=False, window=(100,200,600,500))
camera.framerate = 15
cnt = 20

while True:
	x = raw_input()

	if x == "s":
		camera.stop_preview()
		break
	elif x == "c":
		path = '/home/plate/Desktop/image'+str(cnt)+'.png'
		camera.capture(path)

		cnt += 1

	else:
		print(x)




