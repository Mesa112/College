import subprocess
import sys
import random
from PyQt5.QtWidgets import QApplication, QWidget, QLabel, QPushButton, QVBoxLayout
from PyQt5.QtGui import QFont
from PyQt5.QtCore import Qt

class main_exe:
    def initialize(self, python_path, cpp_path, histogram_path_reg, histogram_path_sp):
        self.python_path = python_path
        self.cpp_path = cpp_path
        self.histogram_path_reg = histogram_path_reg
        self.histogram_path_sp = histogram_path_sp
        
    def execute_requirements(self):
        try:
            subprocess.run(["python3", self.python_path], check=True)
            subprocess.run([self.cpp_path], check=True)
        except subprocess.CalledProcessError as e:
            print(f"An error occurred while running the subprocess: {e}")
            print(f"Return code: {e.returncode}")
        except FileNotFoundError as e:
            print(f"Executable not found: {e}")
        except Exception as e:
            print(f"An unexpected error occurred: {e}")
            
    def process_requirement(self):
        try:
            with open(self.histogram_path_reg, 'r') as hist_reg:
                stored_count = []
                while True: 
                    line = hist_reg.readline()
                    if not line:
                        break
                    lines = line.split(': ')
                    if len(lines) == 2:  # making sure that we split it correctly
                        number = int(lines[0].strip())
                        stars = lines[1].strip()
                        star_count = stars.count('*')
                        stored_count.append((number, star_count))
                        
                select_nums = set()
                
                while len(select_nums) < 4:   
                    random_number = random.randint(1, 70)
                    for num, count in stored_count:
                        if num == random_number and count > 1:
                            select_nums.add(random_number)
                            break
            
                while len(select_nums) < 5:
                    random_number = random.randint(1, 70)
                    for num, count in stored_count:
                        if num == random_number and count == 1:
                            select_nums.add(random_number)
                            break

            select_nums = sorted(select_nums)  # Sorting the selected numbers

            with open(self.histogram_path_sp, 'r') as hist_sp:
                stored_count_sp = []
                while True: 
                    line = hist_sp.readline()
                    if not line:
                        break
                    lines = line.split(': ')
                    if len(lines) == 2:  # making sure that we split it correctly
                        number = int(lines[0].strip())
                        stars = lines[1].strip()
                        star_count = stars.count('*')
                        stored_count_sp.append((number, star_count))

                special_num = None
                while not special_num:
                    random_number = random.randint(1, 25)
                    for num, count in stored_count_sp:
                        if num == random_number and count > 0:
                            special_num = random_number
                            break
                
            return select_nums, special_num
            
        except FileNotFoundError:
            print(f"Histogram file not found: {self.histogram_path_reg} or {self.histogram_path_sp}")
            return [], None
        except Exception as e:
            print(f"An error occurred while processing the histogram: {e}")       
            return [], None

class app_gui(QWidget):
    def __init__(self):
        super().__init__()
        self.main_exe = main_exe()
        self.main_exe.initialize(
            '/Users/estebanm/Desktop/RandomNum/import', 
            '/Users/estebanm/Desktop/RandomNum/megHist', 
            '/Users/estebanm/Desktop/RandomNum/winnerRegHist.txt', 
            '/Users/estebanm/Desktop/RandomNum/winnerSpHist.txt'
        )
        
        self.initUI()
        
    def initUI(self):
        self.setGeometry(100, 100, 400, 300)  # Set window size and position
        self.setWindowTitle('Randomize Your Pick!')
        
        self.label = QLabel("Get your Quick Pick Numbers", self)
        self.label.setFont(QFont('Arial', 14))
        self.label.setAlignment(Qt.AlignCenter)
        
        self.result_label = QLabel("", self)
        self.result_label.setFont(QFont('Courier New', 18))
        self.result_label.setAlignment(Qt.AlignCenter)
        self.result_label.setStyleSheet("color: black;")
        
        self.btn = QPushButton('Process Requirements', self)
        self.btn.setFont(QFont('Arial', 12))
        self.btn.setStyleSheet("background-color: green; color: white;")
        self.btn.clicked.connect(self.on_click)
        
        self.btn2 = QPushButton('Update Data', self)
        self.btn2.setFont(QFont('Courier New', 12))
        self.btn2.setStyleSheet("background-color: green; color: white;")
        self.btn.clicked.connect(self.update_data)
        
        
        vbox = QVBoxLayout()
        vbox.addWidget(self.label)
        vbox.addWidget(self.result_label)
        vbox.addWidget(self.btn)
        
        self.setLayout(vbox)
        
        self.show()
        
    def on_click(self):
        select_nums, special_num = self.main_exe.process_requirement()
        if select_nums and special_num is not None:
            self.result_label.setText(f"Selected numbers: {select_nums}\nSpecial number: {special_num}")
        else:
            self.result_label.setText("An error occurred while processing the requirements")
            
    def update_data(self):
        self.main_exe.execute_requirements()  # Execute the C++ and Python scripts

if __name__ == '__main__':
    app = QApplication(sys.argv)
    ex = app_gui()
    sys.exit(app.exec_())
