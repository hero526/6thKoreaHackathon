#!/usr/bin/etc python
# -*- coding: utf-8 -*-
import sys
import re

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
        img_canny=cv2.Canny(img_blur,80,100)
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

            if  (rect_area>=1500)and(rect_area<=30000)and(aspect_ratio>=0.35)and(aspect_ratio<=0.8): 
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
                if gradient<0.15:
                    count=count+1
               #measure number plate size         
                if count > f_count:
                    select = m
                    f_count = count
                    plate_width=delta_x
        cv2.imwrite('snake.jpg',img)

        hight_top = box1[select][1]-box1[select][1]/7
        hight_btm = box1[select][3]+box1[select][1]+30

        pre_num = ""
        post_num = ""

        num = ""
       
        for i in range(1, 10):
            # move picture according to the first predicted value
            plate = copy_img[hight_top:hight_btm, box1[select][0]-20-(i*10) : box1[select][0]+440+i*20]

            resize_plate=cv2.resize(plate,None,fx=1,fy=1,interpolation=cv2.INTER_CUBIC+cv2.INTER_LINEAR)
            plate_gray=cv2.cvtColor(resize_plate,cv2.COLOR_BGR2GRAY)
            ret,th_plate = cv2.threshold(plate_gray,50,255,cv2.THRESH_BINARY)
            cv2.imwrite('plate_th1.jpg',th_plate)
            num = pytesseract.image_to_string(Image.open('plate_th1.jpg'), lang='kor', config='-c preserve_interword_spaces=1 -l kor --oem 3 --psm 8')
            print(str(i) +' fst: ' + num)

            if self.check_post(num):
                post_num = num[:4]
                break
            elif self.check_pre(num):
                pre_num = num[-3:]
                break

        if post_num == "":
            for i in range(1, 10):
                plate = copy_img[hight_top:hight_btm, box1[select][0]+500-(i*20):box1[select][0]+950+(i*20)]
                resize_plate=cv2.resize(plate,None,fx=1,fy=1,interpolation=cv2.INTER_CUBIC+cv2.INTER_LINEAR)
                plate_gray=cv2.cvtColor(resize_plate,cv2.COLOR_BGR2GRAY)
                ret,th_plate = cv2.threshold(plate_gray,50,255,cv2.THRESH_BINARY)

                cv2.imwrite('plate_th2.jpg',th_plate)
                num = pytesseract.image_to_string(Image.open('plate_th2.jpg'), lang='kor', config='-c preserve_interword_spaces=1 -l kor --oem 3 --psm 8')
                print(str(i) + ' post: ' + num)
                if self.check_post(num):
                    post_num = num[:4]
                    break
            
        elif pre_num == "":
            for i in range(1, 10):
                plate = copy_img[hight_top:hight_btm,box1[select][0]-450-(i*20):box1[select][0]-20+i]

                resize_plate=cv2.resize(plate,None,fx=1,fy=1,interpolation=cv2.INTER_CUBIC+cv2.INTER_LINEAR)
                plate_gray=cv2.cvtColor(resize_plate,cv2.COLOR_BGR2GRAY)
                ret,th_plate = cv2.threshold(plate_gray,50,255,cv2.THRESH_BINARY)

                cv2.imwrite('plate_th2.jpg',th_plate)
                num = pytesseract.image_to_string(Image.open('plate_th2.jpg'), lang='kor', config='-c preserve_interword_spaces=1 -l kor --oem 3 --psm 8')
                print(str(i) +' pre: ' + num)
                if self.check_pre(num):
                    pre_num = num[-3:]
                    break

        result = pre_num + " " + post_num
        if self.check_pre(pre_num) and self.check_post(post_num):
            return result
        else:
            return "Failed"
    def check_pre(self, arg_text):
        if len(arg_text) < 3:
            return
        text = arg_text[-3:]
        pre_pattern = re.compile(u'^\d\d[\u3130-\u318F\uAC00-\uD7A3]$')

        hanCount = len(re.findall(u'[\u3130-\u318F\uAC00-\uD7A3]+', text))
        print("hancount: "+ str(hanCount))

        return hanCount > 0 and re.match(pre_pattern, text)

    def check_post(self, arg_text):
        if len(arg_text) < 4:
            return
        text = arg_text[:4]
        post_pattern = re.compile(u'^\d\d\d\d$')

        dCount = len(re.findall('\d', text))
        print("dcount:"+ str(dCount))

        return dCount ==  4 and re.match(post_pattern, text)


    

