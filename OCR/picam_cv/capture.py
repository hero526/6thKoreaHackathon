from picamera import PiCamera


camera = PiCamera()
camera.start_preview()
cnt = 10

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




