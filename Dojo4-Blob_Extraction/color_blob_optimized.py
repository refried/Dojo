#!/usr/bin/env python
import cv2
import cv
import numpy as np
import math
import time
from random import randint

HUE_MEAN = 126.993
SATURATION_MEAN = 153.822

HUE_VARIANCE = 41.2302
SATURATION_VARIANCE = 1333.83

LOW_HSV = [HUE_MEAN - math.sqrt(HUE_VARIANCE), SATURATION_MEAN - math.sqrt(SATURATION_VARIANCE), 40]

HIGH_HSV = [HUE_MEAN + math.sqrt(HUE_VARIANCE), SATURATION_MEAN + math.sqrt(SATURATION_VARIANCE), 230]

#GLOBAL IMAGES
hsv_img = None
bin_img = None
binaryImg = None 
tempImg = None

# Get binary thresholded image
# low_HSV, hi_HSV - low, high range values for threshold as a list [H,S,V]
# debug= True to display the binary image generated
def get_binary(src_img, low_HSV, hi_HSV, debug=False):
    global hsv_img, bin_img
    #convert to HSV
    hsv_img = cv2.cvtColor(src_img, cv.CV_BGR2HSV)
    #generate binary image
    lower = np.array(low_HSV)
    higher = np.array(hi_HSV)
    bin_img = cv2.inRange(hsv_img, lower, higher)
    if debug:
        cv2.namedWindow("Binary")
        cv2.imshow("Binary",bin_img)
        cv2.waitKey(0)
        cv2.destroyWindow("Binary")
        
    return bin_img



#extract color blobs
def extract_color_blob(img): 	
    global tempImg, binaryImg

    start = time.clock()
    #binarize the image
    binaryImg = get_binary(img, LOW_HSV, HIGH_HSV, False) 
    print 'Binarizing Time = ', time.clock()-start

    imgSize = np.shape(binaryImg)
    #create temp image
    #tempImg = np.zeros(imgSize)
    lastValue = 0
    nextLabel = 1
    pairs = []
    blobs = []

    start = time.clock()

    for i in range(imgSize[0]):
        for j in range(imgSize[1]):
            if (binaryImg.item(i,j) != 0):
                upLabel = tempImg.item(i-1,j) if i > 0 else 0
                leftLabel = tempImg.item(i, j-1) if j > 0 else 0
                if (upLabel == leftLabel):
                    # generate new label if theyre both 0s
                    if (upLabel == 0):
                        upLabel = nextLabel
                        nextLabel += 1
                    tempImg[i,j] = upLabel #or leftLabel, since they're equal
                elif upLabel == 0:
                    # upLabel is 0, use the left label
                    tempImg[i,j] = leftLabel
                elif leftLabel == 0:
                    # leftLabel is 0, use the up label
                    tempImg[i,j] = upLabel 
                else:
                    tempImg[i,j] = min(upLabel, leftLabel)
                    index = -1;
                    for blob in range(len(blobs)):
                        # check if there's a list with either uplabel or leftlabel
                        if(upLabel in blobs[blob] or leftLabel in blobs[blob]):
                            if(index == -1):
                                blobs[blob] = blobs[blob].union({upLabel, leftLabel})
                                index = blob
                            else:
                               blobs[index] = blobs[index].union(blobs[blob])
                               blobs[blob] = {}
                               break
                               
                    if(index == -1):
                        blobs.append({upLabel, leftLabel})
            else:
                tempImg[i,j] = 0
    
    print 'Connected Components =', time.clock()-start
    #remove empty sets
    temp_blobs = []
    for blob in blobs:
        if len(blob) > 0:
            temp_blobs.append(blob)
    blobs = temp_blobs        
    

    #create dictionary to reference blob no.s
    label_dict = {0:0}
    for blob_no in range(len(blobs)):
        if len(blobs[blob_no]) > 0:
            min_no = min(blobs[blob_no])
            for label_no in blobs[blob_no]:
                label_dict[label_no] = int(min_no)
        else:
            #remove empty lists
            del blobs[blob_no]
    
    #append dictionary for all possible blobs
    for i in range(nextLabel):
        if i+1 not in label_dict:
            label_dict[i+1] = int(i+1)
    

    print "start"
    start = time.clock()
    tempImgForReal = np.zeros((imgSize[0], imgSize[1], 3))
    colors = [[255,0,0], [0,255,0], [0,0,255]]
    for i in range(imgSize[0]):
        for j in range(imgSize[1]):
            temper = int(label_dict[int(tempImg.item(i,j))])
            tempImg[i,j] = temper
            if temper > 0:
                color = temper%3
                tempImgForReal.itemset((i,j,color),255)
            #tempImgForReal[i,j] = colors[ temper % 3] if temper > 0 else [0,0,0]
    print "time : " ,  time.clock() - start                                     
    '''print blobs
    print "nextLabel=",nextLabel
    print "equiv pairs=",pairs'''


    cv2.namedWindow("Temp img")
    cv2.imshow("Temp img",tempImgForReal)
    cv2.waitKey(10)

    return


def main():
    global tempImg, tempImgForReal, hsv_img, bin_img, binaryImg

    cam = cv2.VideoCapture()

    cam.open("test_vid.mov")
    cv2.namedWindow("Test",0)

    frameCount = 0

    if cam.isOpened():
        c= None
        
        while not c==27:
            success, M = cam.read()

            if not success:
                print "Couldn't get next frame"
                return
            if frameCount == 0:
                imgSize = np.shape(M)
                print imgSize
                hsv_img = np.zeros(imgSize)
                bin_img = np.zeros((imgSize[0], imgSize[1]))
                binaryImg = np.zeros((imgSize[0], imgSize[1]))
                tempImg = np.zeros((imgSize[0], imgSize[1]))
                tempImgForReal = np.zeros(imgSize)

            if frameCount>0:
                start = time.clock()
                extract_color_blob(M)
                print "Total = ", time.clock()-start
            cv2.imshow("Test", M)                                    
            c = cv2.waitKey(10)
            frameCount += 1
    else:
        print 'Failed to open video'
        return

if __name__ == '__main__':
    main()
