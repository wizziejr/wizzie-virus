# Example in Python using Flask
from flask import Flask, request, jsonify

app = Flask(__name__)

# Endpoint to receive data from the spyware
@app.route('/data', methods=['POST'])
def receive_data():
 data = request.json
 # Process the received data
 return jsonify({"status": "success"}), 200

if __name__ == '__main__':
 app.run(host='0.0.0.0', port=5000)
