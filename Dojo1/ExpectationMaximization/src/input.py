'''
Created on Jun 13, 2013

@author: hsahni3
'''

import numpy as np
import csv
class DataSet:
    def __init__(self):
        self.num_points = 0
        self.dims = 0
        self.data = None
    
    def parse(self, fileName):
        data = []
        with open(fileName, 'rb') as f:
            for row in f:
                row = row.strip()
                parts = row.split('    ')
                data.append([int(parts[0]), int(parts[1])])
        self.data = np.asarray(data)
        self.num_points = data.__len__()
        self.dims = data[0].__len__()
    


