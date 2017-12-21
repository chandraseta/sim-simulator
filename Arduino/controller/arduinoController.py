import serial
import msvcrt
import sys

Port = sys.argv[1]
ser = serial.Serial(Port,9600,timeout=1)
ser.flushInput()
ser.flushOutput()

exit = False
recvData = 0xAA
command = 0xAA
prevCommand = 0xAA
prevCommand2 = 0xAA
while not exit:
  command = msvcrt.getch()
  print(command)

  if (command == b'w'):
    # move forward
    ser.write(b'F')
  elif (command == b'a'):
    # turn left
    ser.write(b'L')
  elif (command == b's'):
    # backward
    ser.write(b'B')
  elif (command == b'd'):
    # move right
    ser.write(b'R')
  else:
    ser.write(0xAA)

  if (ser.in_waiting > 0):
    recvData = ser.read()
    print(recvData)
  exit = (command == b'\x1b' or command == b'\x03' or recvData == b'\x03')