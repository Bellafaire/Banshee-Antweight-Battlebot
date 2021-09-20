EESchema Schematic File Version 4
EELAYER 30 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 1 1
Title "Banshee - Antweight Battlebot Schematic"
Date "2021-06-27"
Rev "1"
Comp ""
Comment1 "Matthew James Bellafaire "
Comment2 "https://github.com/bellafaire"
Comment3 "https://hackaday.io/bellafaire"
Comment4 ""
$EndDescr
$Comp
L customParts:ESP32-DEVKITC-32D U1
U 1 1 604A4B2A
P 2250 5000
F 0 "U1" H 2250 6167 50  0000 C CNN
F 1 "ESP32-DEVKITC-32D" H 2250 6076 50  0000 C CNN
F 2 "MODULE_ESP32-DEVKITC-32D" H 2250 5000 50  0001 L BNN
F 3 "" H 2250 5000 50  0001 L BNN
F 4 "4" H 2250 5000 50  0001 L BNN "PARTREV"
F 5 "Espressif Systems" H 2250 5000 50  0001 L BNN "MANUFACTURER"
	1    2250 5000
	1    0    0    -1  
$EndComp
$Comp
L Regulator_Linear:AMS1117 U2
U 1 1 604A9FDF
P 2300 1000
F 0 "U2" H 2300 1242 50  0000 C CNN
F 1 "AMS1117" H 2300 1151 50  0000 C CNN
F 2 "Package_TO_SOT_SMD:SOT-223-3_TabPin2" H 2300 1200 50  0001 C CNN
F 3 "http://www.advanced-monolithic.com/pdf/ds1117.pdf" H 2400 750 50  0001 C CNN
	1    2300 1000
	1    0    0    -1  
$EndComp
Wire Wire Line
	2600 1000 3100 1000
$Comp
L power:GND #PWR01
U 1 1 604C55C3
P 1200 1200
F 0 "#PWR01" H 1200 950 50  0001 C CNN
F 1 "GND" H 1200 1050 50  0000 C CNN
F 2 "" H 1200 1200 50  0001 C CNN
F 3 "" H 1200 1200 50  0001 C CNN
	1    1200 1200
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR05
U 1 1 604C62C8
P 2300 1650
F 0 "#PWR05" H 2300 1400 50  0001 C CNN
F 1 "GND" H 2305 1477 50  0000 C CNN
F 2 "" H 2300 1650 50  0001 C CNN
F 3 "" H 2300 1650 50  0001 C CNN
	1    2300 1650
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR07
U 1 1 604C6B65
P 3100 1650
F 0 "#PWR07" H 3100 1400 50  0001 C CNN
F 1 "GND" H 3105 1477 50  0000 C CNN
F 2 "" H 3100 1650 50  0001 C CNN
F 3 "" H 3100 1650 50  0001 C CNN
	1    3100 1650
	1    0    0    -1  
$EndComp
Wire Wire Line
	3100 1200 3100 1000
Connection ~ 3100 1000
Wire Wire Line
	3100 1000 3700 1000
Wire Wire Line
	3100 1500 3100 1650
Wire Wire Line
	2300 1650 2300 1300
$Comp
L power:+5V #PWR08
U 1 1 604C80D5
P 3700 1000
F 0 "#PWR08" H 3700 850 50  0001 C CNN
F 1 "+5V" H 3715 1173 50  0000 C CNN
F 2 "" H 3700 1000 50  0001 C CNN
F 3 "" H 3700 1000 50  0001 C CNN
	1    3700 1000
	1    0    0    -1  
$EndComp
$Comp
L power:+5V #PWR06
U 1 1 604CC6E6
P 2300 2950
F 0 "#PWR06" H 2300 2800 50  0001 C CNN
F 1 "+5V" V 2300 3150 50  0000 C CNN
F 2 "" H 2300 2950 50  0001 C CNN
F 3 "" H 2300 2950 50  0001 C CNN
	1    2300 2950
	1    0    0    -1  
$EndComp
$Comp
L power:+3.3V #PWR03
U 1 1 604CD137
P 2200 2950
F 0 "#PWR03" H 2200 2800 50  0001 C CNN
F 1 "+3.3V" V 2200 3200 50  0000 C CNN
F 2 "" H 2200 2950 50  0001 C CNN
F 3 "" H 2200 2950 50  0001 C CNN
	1    2200 2950
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR04
U 1 1 604CE668
P 2250 6900
F 0 "#PWR04" H 2250 6650 50  0001 C CNN
F 1 "GND" H 2255 6727 50  0000 C CNN
F 2 "" H 2250 6900 50  0001 C CNN
F 3 "" H 2250 6900 50  0001 C CNN
	1    2250 6900
	1    0    0    -1  
$EndComp
Wire Wire Line
	2150 6800 2150 6900
Wire Wire Line
	2150 6900 2250 6900
Wire Wire Line
	2350 6900 2350 6800
Connection ~ 2250 6900
Wire Wire Line
	2250 6900 2350 6900
Wire Wire Line
	2250 6800 2250 6900
Wire Notes Line
	650  550  4000 550 
Wire Notes Line
	4000 2200 650  2200
Wire Notes Line
	4000 550  4000 2200
Wire Notes Line
	650  550  650  2200
Text Notes 700  650  0    50   ~ 0
Battery Power In\n
$Comp
L Connector:Conn_01x03_Male J3
U 1 1 604E21A6
P 5350 4250
F 0 "J3" H 5322 4182 50  0000 R CNN
F 1 "Conn_01x03_Male" H 5322 4273 50  0000 R CNN
F 2 "Connector_PinHeader_2.54mm:PinHeader_1x03_P2.54mm_Vertical" H 5350 4250 50  0001 C CNN
F 3 "~" H 5350 4250 50  0001 C CNN
	1    5350 4250
	-1   0    0    1   
$EndComp
Wire Wire Line
	6200 1600 6700 1600
Wire Wire Line
	6200 1800 6700 1800
Wire Wire Line
	6200 2200 6700 2200
$Comp
L Driver_Motor:L293D U3
U 1 1 604A663A
P 5700 2200
F 0 "U3" H 5200 3400 50  0000 C CNN
F 1 "L293D" H 5250 3300 50  0000 C CNN
F 2 "Package_DIP:DIP-16_W7.62mm" H 5950 1450 50  0001 L CNN
F 3 "http://www.ti.com/lit/ds/symlink/l293.pdf" H 5400 2900 50  0001 C CNN
	1    5700 2200
	1    0    0    -1  
$EndComp
Wire Wire Line
	6200 2400 6700 2400
$Comp
L power:GND #PWR012
U 1 1 604F503C
P 5700 3150
F 0 "#PWR012" H 5700 2900 50  0001 C CNN
F 1 "GND" H 5705 2977 50  0000 C CNN
F 2 "" H 5700 3150 50  0001 C CNN
F 3 "" H 5700 3150 50  0001 C CNN
	1    5700 3150
	1    0    0    -1  
$EndComp
Wire Wire Line
	5500 3000 5500 3150
Wire Wire Line
	5500 3150 5600 3150
Wire Wire Line
	5900 3150 5900 3000
Connection ~ 5700 3150
Wire Wire Line
	5700 3150 5800 3150
Wire Wire Line
	5800 3000 5800 3150
Connection ~ 5800 3150
Wire Wire Line
	5800 3150 5900 3150
Wire Wire Line
	5600 3000 5600 3150
Connection ~ 5600 3150
Wire Wire Line
	5600 3150 5700 3150
$Comp
L power:+3.3V #PWR011
U 1 1 604F67A1
P 5600 1200
F 0 "#PWR011" H 5600 1050 50  0001 C CNN
F 1 "+3.3V" V 5600 1450 50  0000 C CNN
F 2 "" H 5600 1200 50  0001 C CNN
F 3 "" H 5600 1200 50  0001 C CNN
	1    5600 1200
	1    0    0    -1  
$EndComp
$Comp
L power:+5V #PWR013
U 1 1 604F7A16
P 5800 1200
F 0 "#PWR013" H 5800 1050 50  0001 C CNN
F 1 "+5V" V 5800 1400 50  0000 C CNN
F 2 "" H 5800 1200 50  0001 C CNN
F 3 "" H 5800 1200 50  0001 C CNN
	1    5800 1200
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR010
U 1 1 604FDF8A
P 4900 4450
F 0 "#PWR010" H 4900 4200 50  0001 C CNN
F 1 "GND" H 4905 4277 50  0000 C CNN
F 2 "" H 4900 4450 50  0001 C CNN
F 3 "" H 4900 4450 50  0001 C CNN
	1    4900 4450
	1    0    0    -1  
$EndComp
$Comp
L power:+5V #PWR09
U 1 1 604FEAA7
P 4400 4250
F 0 "#PWR09" H 4400 4100 50  0001 C CNN
F 1 "+5V" V 4400 4450 50  0000 C CNN
F 2 "" H 4400 4250 50  0001 C CNN
F 3 "" H 4400 4250 50  0001 C CNN
	1    4400 4250
	1    0    0    -1  
$EndComp
Wire Wire Line
	5150 4150 4550 4150
Text Label 4550 4150 0    50   ~ 0
ESC_Sig
Wire Wire Line
	4400 4250 5150 4250
Wire Wire Line
	4900 4450 4900 4350
Wire Wire Line
	4900 4350 5150 4350
Text Label 3600 5500 2    50   ~ 0
ESC_Sig
Wire Wire Line
	3600 5500 3050 5500
Text Label 4600 1600 0    50   ~ 0
Motor_1A
Text Label 4600 1800 0    50   ~ 0
Motor_2A
Text Label 4600 2000 0    50   ~ 0
Motor_1EN
Text Label 4600 2200 0    50   ~ 0
Motor_3A
Text Label 4600 2400 0    50   ~ 0
Motor_4A
Text Label 4600 2600 0    50   ~ 0
Motor_2EN
Wire Wire Line
	4600 1600 5200 1600
Wire Wire Line
	4600 1800 5200 1800
Wire Wire Line
	4600 2000 5200 2000
Wire Wire Line
	4600 2200 5200 2200
Wire Wire Line
	4600 2400 5200 2400
Wire Wire Line
	4600 2600 5200 2600
Text Label 3650 4900 2    50   ~ 0
Motor_1A
Text Label 3650 5000 2    50   ~ 0
Motor_2A
Text Label 3650 4800 2    50   ~ 0
Motor_1EN
Text Label 3650 5900 2    50   ~ 0
Motor_3A
Text Label 3650 5800 2    50   ~ 0
Motor_4A
Text Label 3650 6000 2    50   ~ 0
Motor_2EN
Wire Wire Line
	3650 4900 3050 4900
Wire Wire Line
	3650 5000 3050 5000
Wire Wire Line
	3650 4800 3050 4800
Wire Wire Line
	3650 5900 3050 5900
Wire Wire Line
	3650 5800 3050 5800
Wire Wire Line
	3650 6000 3050 6000
Wire Notes Line
	650  2400 4000 2400
Wire Notes Line
	4000 2400 4000 7700
Wire Notes Line
	4000 7700 650  7700
Wire Notes Line
	650  7700 650  2400
Wire Notes Line
	8950 550  8950 3550
Wire Notes Line
	4100 3550 4100 550 
Text Label 6700 1600 2    50   ~ 0
M1+
Text Label 6700 1800 2    50   ~ 0
M1-
Text Label 6700 2400 2    50   ~ 0
M2-
Text Label 6700 2200 2    50   ~ 0
M2+
Wire Wire Line
	8200 2450 7700 2450
Wire Wire Line
	8200 2350 7700 2350
Wire Wire Line
	8200 1700 7700 1700
Wire Wire Line
	8200 1600 7700 1600
Text Label 7700 2450 0    50   ~ 0
M1+
Text Label 7700 2350 0    50   ~ 0
M1-
Text Label 7700 1600 0    50   ~ 0
M2-
Text Label 7700 1700 0    50   ~ 0
M2+
$Comp
L Device:D D7
U 1 1 60546499
P 1500 1000
F 0 "D7" V 1454 1080 50  0000 L CNN
F 1 "1N4002" V 1545 1080 50  0000 L CNN
F 2 "Diode_THT:D_A-405_P12.70mm_Horizontal" H 1500 1000 50  0001 C CNN
F 3 "~" H 1500 1000 50  0001 C CNN
	1    1500 1000
	-1   0    0    1   
$EndComp
Wire Notes Line
	4100 550  8950 550 
Wire Notes Line
	4100 3550 8950 3550
Text Notes 4200 650  0    50   ~ 0
DC Motor Driver
Text Notes 4150 3750 0    50   ~ 0
Brushless Weapon Control Signal\n
Wire Notes Line
	4100 3600 4100 4800
Wire Notes Line
	4100 4800 6450 4800
Wire Notes Line
	6450 4800 6450 3600
Wire Notes Line
	6450 3600 4100 3600
Text Notes 700  2550 0    50   ~ 0
ESP32
Wire Wire Line
	1050 1000 1350 1000
Wire Wire Line
	1650 1000 2000 1000
$Comp
L Connector:Conn_01x02_Female J?
U 1 1 604CF496
P 850 1100
F 0 "J?" H 742 775 50  0000 C CNN
F 1 "Conn_01x02_Female" H 742 866 50  0000 C CNN
F 2 "" H 850 1100 50  0001 C CNN
F 3 "~" H 850 1100 50  0001 C CNN
	1    850  1100
	-1   0    0    1   
$EndComp
Wire Wire Line
	1050 1100 1200 1100
Wire Wire Line
	1200 1100 1200 1200
Text Label 1150 1000 0    50   ~ 0
Bat+
$Comp
L Connector:Conn_01x02_Female J?
U 1 1 604D185A
P 850 1800
F 0 "J?" H 742 1475 50  0000 C CNN
F 1 "Conn_01x02_Female" H 600 1600 50  0000 C CNN
F 2 "" H 850 1800 50  0001 C CNN
F 3 "~" H 850 1800 50  0001 C CNN
	1    850  1800
	-1   0    0    1   
$EndComp
Wire Wire Line
	1050 1700 1300 1700
Wire Wire Line
	1050 1800 1300 1800
Wire Wire Line
	1300 1800 1300 1900
$Comp
L power:GND #PWR?
U 1 1 604D673E
P 1300 1900
F 0 "#PWR?" H 1300 1650 50  0001 C CNN
F 1 "GND" H 1300 1750 50  0000 C CNN
F 2 "" H 1300 1900 50  0001 C CNN
F 3 "" H 1300 1900 50  0001 C CNN
	1    1300 1900
	1    0    0    -1  
$EndComp
Text Label 1300 1700 2    50   ~ 0
Bat+
Text Notes 3150 2150 0    50   ~ 0
*XT30U connectors\n
$Comp
L Device:CP1 C?
U 1 1 604D7633
P 3100 1350
F 0 "C?" H 3215 1396 50  0000 L CNN
F 1 "470u" H 3215 1305 50  0000 L CNN
F 2 "" H 3100 1350 50  0001 C CNN
F 3 "~" H 3100 1350 50  0001 C CNN
	1    3100 1350
	1    0    0    -1  
$EndComp
$Comp
L Connector:Conn_01x02_Female J?
U 1 1 604D80C0
P 8400 1600
F 0 "J?" H 7650 1750 50  0000 C CNN
F 1 "Conn_01x02_Female" H 8200 1750 50  0000 C CNN
F 2 "" H 8400 1600 50  0001 C CNN
F 3 "~" H 8400 1600 50  0001 C CNN
	1    8400 1600
	1    0    0    -1  
$EndComp
$Comp
L Connector:Conn_01x02_Female J?
U 1 1 604DB378
P 8400 2350
F 0 "J?" H 7650 2500 50  0000 C CNN
F 1 "Conn_01x02_Female" H 8200 2500 50  0000 C CNN
F 2 "" H 8400 2350 50  0001 C CNN
F 3 "~" H 8400 2350 50  0001 C CNN
	1    8400 2350
	1    0    0    -1  
$EndComp
$Comp
L Device:R R?
U 1 1 604C396B
P 5200 5400
F 0 "R?" H 5270 5446 50  0000 L CNN
F 1 "22k" H 5270 5355 50  0000 L CNN
F 2 "" V 5130 5400 50  0001 C CNN
F 3 "~" H 5200 5400 50  0001 C CNN
	1    5200 5400
	1    0    0    -1  
$EndComp
$Comp
L Device:R R?
U 1 1 604C42A2
P 5200 5900
F 0 "R?" H 5270 5946 50  0000 L CNN
F 1 "2.2k" H 5270 5855 50  0000 L CNN
F 2 "" V 5130 5900 50  0001 C CNN
F 3 "~" H 5200 5900 50  0001 C CNN
	1    5200 5900
	1    0    0    -1  
$EndComp
Text Label 5350 5100 0    50   ~ 0
Bat+
Wire Wire Line
	5350 5100 5200 5100
Wire Wire Line
	5200 5100 5200 5250
Wire Wire Line
	5200 5550 5200 5650
$Comp
L power:GND #PWR?
U 1 1 604C94FF
P 5200 6250
F 0 "#PWR?" H 5200 6000 50  0001 C CNN
F 1 "GND" H 5205 6077 50  0000 C CNN
F 2 "" H 5200 6250 50  0001 C CNN
F 3 "" H 5200 6250 50  0001 C CNN
	1    5200 6250
	1    0    0    -1  
$EndComp
Wire Wire Line
	5200 6250 5200 6050
Text Label 5700 5650 2    50   ~ 0
BAT_SENSE
Text Label 3650 6100 2    50   ~ 0
BAT_SENSE
Wire Wire Line
	3650 6100 3050 6100
Wire Wire Line
	5700 5650 5200 5650
Connection ~ 5200 5650
Wire Wire Line
	5200 5650 5200 5750
Wire Notes Line
	4100 4900 6450 4900
Wire Notes Line
	6450 4900 6450 6600
Wire Notes Line
	6450 6600 4100 6600
Wire Notes Line
	4100 6600 4100 4900
Text Notes 4150 5050 0    50   ~ 0
Battery Sense
$EndSCHEMATC
