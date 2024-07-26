import requests
from bs4 import BeautifulSoup
from datetime import datetime

# URL of the Texas Lottery Mega Millions Winning Numbers page
url = "https://www.texaslottery.com/export/sites/lottery/Games/Mega_Millions/Winning_Numbers/index.html_2013354932.html"

# Function to parse the date and check if it's after October 28, 2017
def is_after_date(date_str, format="%m/%d/%Y"):
    date = datetime.strptime(date_str, format)
    return date >= datetime(2017, 10, 28)

response = requests.get(url)
if response.status_code == 200:
    soup = BeautifulSoup(response.content, 'html.parser')
    
    # Attempt to find the table directly
    table = soup.find('table')
    if table:
        # Open a text file to write the data
        with open('/Users/estebanm/Desktop/RandomNum/mega_millions_winning_numbers.txt', 'w') as txtfile:
            # Loop through the rows of the table
            for row in table.find_all('tr')[1:]:
                cols = row.find_all('td')
                if len(cols) >= 7:  # Adjusted to handle unexpected column counts
                    date = cols[0].text.strip()
                    if is_after_date(date):
                        numbers_str = cols[1].text.strip()
                        numbers = [num for num in numbers_str.split(' ') if num != '-']
                        while len(numbers) < 5:  # Ensure we have 5 numbers
                            numbers.append('')
                        megaball = cols[2].text.strip()
                        winner = cols[5].text.strip()
                        txtfile.write(' '.join(numbers + [megaball] + [winner]) + '\n')
        print("Data scraped and saved to mega_millions_winning_numbers.txt")
    else:
        print("Could not find the table on the page.")
else:
    print(f"Failed to retrieve the webpage. Status code: {response.status_code}")
