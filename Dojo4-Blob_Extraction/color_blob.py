#!/usr/bin/env python
import cv2
import cv
import numpy as np
import math

HUE_MEAN = 126.993
SATURATION_MEAN = 153.822

HUE_VARIANCE = 41.2302
SATURATION_VARIANCE = 1333.83

LOW_HSV = [HUE_MEAN - math.sqrt(HUE_VARIANCE), SATURATION_MEAN - math.sqrt(SATURATION_VARIANCE), 40]

HIGH_HSV = [HUE_MEAN + math.sqrt(HUE_VARIANCE), SATURATION_MEAN + math.sqrt(SATURATION_VARIANCE), 230]
 
# Get binary thresholded image
# low_HSV, hi_HSV - low, high range values for threshold as a list [H,S,V]
# debug= True to display the binary image generated
def get_binary(src_img, low_HSV, hi_HSV, debug=False):
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

from random import randint
import time
#extract color blobs
def extract_color_blob(img): 	
    #binarize the image
    binaryImg = get_binary(img, LOW_HSV, HIGH_HSV, False) 
    imgSize = np.shape(binaryImg)
    #create temp image
    tempImg = np.zeros(imgSize)
    lastValue = 0
    nextLabel = 1
    pairs = []
    blobs = []
    for i in range(imgSize[0]):
        for j in range(imgSize[1]):
            if (binaryImg[i,j] != 0):
                upLabel = tempImg[i-1,j] if i > 0 else 0
                leftLabel = tempImg[i, j-1] if j > 0 else 0
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
                    pair = (upLabel, leftLabel)
                    revPair = (leftLabel, upLabel)
                    if (pair not in pairs and revPair not in pairs):                
                        pairs.append(pair)
                                            
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
    print "start"
    start = time.clock()           
    for i in range(imgSize[0]):
        for j in range(imgSize[1]):
            for blob in range(len(blobs)):
                if tempImg[i,j] in blobs[blob]:
                    tempImg[i,j] = blob + 1
                    break
    print "time : " ,  time.clock() - start                                     
    '''print blobs
    print "nextLabel=",nextLabel
    print "equiv pairs=",pairs'''
    colors = [[255,0,0], [0,255,0], [0,0,255]]

    tempImgForReal = np.zeros((imgSize[0], imgSize[1], 3))
    for i in range(imgSize[0]):
        for j in range(imgSize[1]):
            tempImgForReal[i,j] = np.array(colors[int(tempImg[i,j]) % 3] if tempImg[i,j] > 0 else [0,0,0])
    
    
    cv2.namedWindow("Temp img")
    cv2.imshow("Temp img",tempImgForReal)
    cv2.waitKey(0)
    cv2.destroyWindow("Temp img")
    return


def main():
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
            if frameCount>200:
                extract_color_blob(M)

            cv2.imshow("Test", M)                                    
            c = cv2.waitKey(60)
            frameCount += 1
    else:
        print 'Failed to open video'
        return

if __name__ == '__main__':
    main()
