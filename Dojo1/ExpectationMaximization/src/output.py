'''
Created on Jun 13, 2013

@author: hsahni3
'''

import matplotlib.pyplot as plt
from pylab import *

class Plotter(object):
    '''
    classdocs
    '''


    def __init__(self):
        '''
        Constructor
        '''
        self.dims = 2
        self.clusters = 0
        self.num_points = 0
        
        
    def plot(self, data_set, classifications):
        fig = plt.figure()
        plt.scatter(data_set.data[:,0], data_set.data[:,1], c = classifications, cmap = cm.prism)
        plt.show()