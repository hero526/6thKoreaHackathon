from picamera import PiCamera
from ocr import Recognition

camera = PiCamera()
#camera.start_preview()
cnt = 1
recogtest = Recognition()
path = 'image10.png'
result=recogtest.ExtractNumber(path)
print(result)
'''
while True:
	x = raw_input()

	if x == "s":
#		camera.stop_preview()
		break
	elif x == "c":
		path = '/home/plate/Desktop/image'+str(cnt)+'.png'
#		camera.capture(path)

		result=recogtest.ExtractNumber(path)
#		cnt += 1
		print(result)
	else:
		print(x)

'''


