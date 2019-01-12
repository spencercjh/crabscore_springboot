import json
import platform

from flask import request, Flask, render_template

app = Flask(__name__, template_folder='../templates')


# noinspection PyUnresolvedReferences
@app.route('/')
def home():
    return render_template('home.html')


@app.route('/generate', methods=['POST'])
def get_data():
    if request.method == 'POST':
        all_company = request.form['all_company']
        all_company = json.loads(all_company)
        print(str(all_company))
        all_group = request.form['all_group']
        print(str(all_group))
        return "generate excel"


if __name__ == '__main__':
    if 'WindowsPE' in platform.architecture():
        app.run(host="127.0.0.1", port=5000)
    else:
        app.run(host="0.0.0.0", port=5000)
