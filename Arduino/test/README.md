![Arduino Uno Pinmap](https://upload.wikimedia.org/wikipedia/commons/thumb/c/c9/Pinout_of_ARDUINO_Board_and_ATMega328PU.svg/2000px-Pinout_of_ARDUINO_Board_and_ATMega328PU.svg.png)

# Ultrasonic
Done

## Bahan
- [x] Sensor HC-SR04 (Rp35000)
- [x] Kabel: MF 4 (ambil dari AVRG)

## Pinmap
![](http://howtomechatronics.com/wp-content/uploads/2015/07/Ultrasonic-Sensor-Cirucit-Schematics-04.png)
- Trig -> A0
- Echo -> A1

## Referensi
- [How-To Mechatronics](http://howtomechatronics.com/tutorials/arduino/ultrasonic-sensor-hc-sr04/)

# Limit Switch
Done

## Bahan
- [x] Limit switch kecil (Rp2500)
- [x] Kabel: M- 2, MM 1 (incl. + ambil dari AVRG)
- [x] Resistor 10k&#937; x1 (incl.)

## Sirkuit
![](https://img.youtube.com/vi/FMK2dLcaILc/maxresdefault.jpg)

## Pinmap
- 1 -> VCC
- 2 -> 10K Ohm -> GND
	- |-> D7

## Referensi
- [YouTube](https://www.youtube.com/watch?v=FMK2dLcaILc)
	
# LCD
Done

## Bahan
- [x] LCD 16x2 Blue on White + I2C backpack (Rp???)
- [x] Kabel: MF 4 (ambil dari AVRG)

## Pinmap
![](https://www.sunfounder.com/media/wysiwyg/swatches/starter_basic_kit_v2-0_for_arduino/4_i2c_lcd1602/i2c_lcd1602_fri.png)
- SDA -> A4
- SCL -> A5

## Referensi
- [New LiquidCrystal Library](https://bitbucket.org/fmalpartida/new-liquidcrystal/wiki/Home)
- [SunFounder](https://www.sunfounder.com/learn/Sensor-Kit-v2-0-for-Arduino/lesson-1-display-by-i2c-lcd1602-sensor-kit-v2-0-for-arduino.html)
- [Instructables](http://www.instructables.com/id/I2C-LCD-Controller-the-easy-way/)
- [Electro Schematics](http://www.electroschematics.com/12459/arduino-i2c-lcd-backpack-introductory-tutorial/)

# Seven Segment
Seven-segment common anode, sehingga pin tengah pada 7-segment dicolok ke VCC (via resistor 10K&#937)

## Bahan
- [x] 7-segment common anode (Rp???)
- [x] IC Serial-In Parallel-Out 74HC595 (Rp???)
- [x] Resistor 330&#937; x8 (incl.)
- [x] Kabel MM x20 (estimasi) (incl.)

## Pinmap
![Sirkuit](https://www.sunfounder.com/media/wysiwyg/swatches/super-kit-v2-for-Arduino/11_74hc595/74hc595_sche.png)
![Fritzing](https://www.sunfounder.com/media/wysiwyg/swatches/super-kit-v2-for-Arduino/11_74hc595/74hc595_fri.png)

Catatan: sirkuit-sirkuit di lampiran untuk 7-segmentn common cathode. Untuk 7-segment common anode yang digunakan, colok pin 3 dan 8 ke VCC, bukan GND.

## Referensi
- [Datasheet SN54HC595](https://www.sparkfun.com/datasheets/IC/SN74HC595.pdf)
- [Circuit Digest - 7-segment](https://circuitdigest.com/article/7-segment-display)
- [SunFounder](https://www.sunfounder.com/learn/rfid-kit-v2-0-for-arduino/lesson-20-driving-7-segment-display-by-74hc595-rfid-v2-0-for-arduino.html)

# DC Motor
Gerak pelan, kadang-kadang tidak mau jalan (harus dicabut-dicolok ulang). 
PWM hanya mau frekuensi tinggi (> 150) karena motor ada voltase minimal untuk bergerak.
Bingung cara menyambungkan motor ke baterai, sehingga motor ditenagai 5V dari Arduino.

## Bahan
- [x] Motor Driver IC L293D (gratis, bekas tubes URO)
- [x] Baterai 9v (Rp12000) -> entah akan dipakai, bingung sambung ke rangkaian motornya bagaimana
- [x] Kabel MM x10 (incl.)

## Pinmap
![Sirkuit H-bridge](https://cdn.instructables.com/FGP/RAZR/IUOHPI1P/FGPRAZRIUOHPI1P.MEDIUM.jpg)

- 1 -> VCC
- 2 -> Arduino D2
- 3 -> Left motor +
- 4 -> GND
- 5 -> GND
- 6 -> Left motor -
- 7 -> Arduino D3
- 8 -> VCC
- 9 -> VCC
- 10 -> Arduino D4
- 11 -> Right motor +
- 12 -> GND
- 13 -> GND
- 14 -> Right motor -
- 15 -> Arduino D5
- 16 -> VCC

## Referensi
- [Instructables](http://www.instructables.com/id/Arduino-How-to-Control-DC-Motors-With-L293D-Motor-/)

# Integrasi
## Bahan
- [x] Body (gratis, bekas makanan)
- [x] Roda (gratis, bekas tubes URO)
- [x] Caster Ball (Rp25000)

## Pinmap
- Ultrasonic
    - A0: trigger
	- A1: echo
- Limit Switch
    - D7: intercept
- LCD (2, Analog)
    - A4: SDA
    - A5: SCL
- 7-segment
    - D8: Clock (74HC95 11)
    - D9: Data (74HC95 14)
    - D12: Latch (74HC95 12)
- Motor
    - D2: Left Fwd (L293D 2)
	- D10: Left Enable (L293D 1)
    - D3: Left Bwd (L293D 7)
    - D4: Right Fwd (L293D 10)
	- D11: Right Fwd (L293D 9)
    - D5: Right Bwd (L293D 15)
