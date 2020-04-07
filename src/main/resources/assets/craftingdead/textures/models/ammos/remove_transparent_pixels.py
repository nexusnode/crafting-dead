#!/usr/bin/python

import os
import sys
from PIL import Image

try :
	arg = sys.argv[1]
except IndexError :
	print("Usage: format18.py <file|dir>")
	sys.exit(1)

skins = [arg]
success = 0
failure = 0
path = ""
pl = "s"

if os.path.isdir(arg) :
	path = arg
	skins = os.listdir(arg)

for sfile in skins :
	sfile = os.path.join(path, sfile)
	try :
		imi = Image.open(sfile)
	except IOError :
		failure += 1
	else :
		w, h = imi.size
		
		pixels = imi.load();
		
		for i in range(imi.size[0]): # for every pixel:
			for j in range(imi.size[1]):
				pixel = pixels[i,j];
				if pixel[3] != 0:
					if pixel[3] > 90:
						pixels[i,j] = (pixel[0], pixel[1], pixel[2], 255);
					else:
						pixels[i,j] = (0, 0, 0, 0);
		

		imi.save(sfile)
		success += 1

if success == 1 : pl = ""
print("%i skin%s successfully converted, %i failed" % (success, pl, failure))