#!/usr/bin/etc python

import cv2
import numpy as np
import pytesseract
from  PIL import Image

class Recognition:
    def ExtractNumber(self, file_name):
#        file_name=sys.argv[1]
        img=cv2.imread(file_name,cv2.IMREAD_COLOR)
        copy_img=img.copy()
        img_gray=cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
        cv2.imwrite('gray.jpg',img_gray)
        img_blur = cv2.GaussianBlur(img_gray,(3,3),0)
        cv2.imwrite('blur.jpg',img_blur)
        img_canny=cv2.Canny(img_blur,130,200)
        cv2.imwrite('canny.jpg',img_canny)
        #cnts,
        contours,hierarchy = cv2.findContours(img_canny, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

        box1=[]
        f_count=0
        select=0
        plate_width=0
        
        for i in range(len(contours)):
            cnt=contours[i]
            area = cv2.contourArea(cnt)
            x,y,w,h = cv2.boundingRect(cnt)
            rect_area=w*h  #area size 3*0.5 = 1.5
            aspect_ratio = float(w)/h # ratio = width/height   3/0.5 = 6

            if  (rect_area>=980)and(rect_area<=29000)and(aspect_ratio>=0.1)and(aspect_ratio<=0.8): 
                cv2.rectangle(img,(x,y),(x+w,y+h),(0,255,0),1)
                box1.append(cv2.boundingRect(cnt))

        for i in range(len(box1)): ##Buble Sort on python
            for j in range(len(box1)-(i+1)):
                if box1[j][0]>box1[j+1][0]:
                    temp=box1[j]
                    box1[j]=box1[j+1]
                    box1[j+1]=temp

        # to find number plate measureing length between rectangles
        for m in range(len(box1)):
            count=0
            for n in range(m+1,(len(box1)-1)):
                delta_x=abs(box1[n+1][0]-box1[m][0])
                if delta_x > 150:
                    break
                delta_y =abs(box1[n+1][1]-box1[m][1])
                if delta_x ==0:
                    delta_x=1
                if delta_y ==0:
                    delta_y=1           
                gradient =float(delta_y) /float(delta_x)
                if gradient<0.20:
                    count=count+1
               #measure number plate size         
                if count > f_count:
                    select = m
                    f_count = count
                    plate_width=delta_x
        cv2.imwrite('snake.jpg',img)

        #number_plate=copy_img[box1[select][1]-80:box1[select][3]+box1[select][1]+30,box1[select]c[0]-1500:box1[select][0]+1400] 

        post_plate=copy_img[box1[select][1]-80:box1[select][3]+box1[select][1]+30,box1[select][0]:box1[select][0]+400]
        resize_plate=cv2.resize(post_plate,None,fx=1.2,fy=1.2,interpolation=cv2.INTER_CUBIC+cv2.INTER_LINEAR)
        plate_gray=cv2.cvtColor(resize_plate,cv2.COLOR_BGR2GRAY)
        ret,th_plate = cv2.threshold(plate_gray,150,255,cv2.THRESH_BINARY)
          
        cv2.imwrite('post_plate_th.jpg',th_plate)
        #kernel = np.ones((3,3),np.uint8)
        #er_plate = cv2.erode(th_plate,kernel,iterations=1)
        #er_invplate = er_plate
        #cv2.imwrite('er_plate.jpg',er_invplate)

        post_num = pytesseract.image_to_string(Image.open('post_plate_th.jpg'), lang='kor', config='-c preserve_interword_spaces=1 -l kor --oem 3 --psm 8')
		
        pre_plate = list()
        post_num = str(filter(str.isdigit, post_num))
        if len(post_num) == 4:
            pre_plate=copy_img[box1[select][1]-80:box1[select][3]+box1[select][1]+30,box1[select][0]-400:box1[select][0]]
        else:
            pre_plate=copy_img[box1[select][1]-80:box1[select][3]+box1[select][1]+30,box1[select][0]-600:box1[select][0]+400]
        resize_plate=cv2.resize(pre_plate,None,fx=1.2,fy=1.2,interpolation=cv2.INTER_CUBIC+cv2.INTER_LINEAR)
        plate_gray=cv2.cvtColor(resize_plate,cv2.COLOR_BGR2GRAY)
        ret,th_plate = cv2.threshold(plate_gray,150,255,cv2.THRESH_BINARY)
        cv2.imwrite('pre_plate_th.jpg',th_plate)
        pre_num = pytesseract.image_to_string(Image.open('pre_plate_th.jpg'), lang='kor', config='-c preserve_interword_spaces=1 -l kor --oem 3 --psm 8')
        result = result + pre_num + post_num
        return(result.replace(" ",""))





