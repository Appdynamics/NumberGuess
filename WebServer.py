# Python 3 Random Number WebServer
# Generates a pseudo-random number between 1 and 20
# And prints it to HTTP Response as text/plain -- not HTML

from http.server import BaseHTTPRequestHandler, HTTPServer
import time
from random import randint

# Change hostName to IP address or hostname that works for your environment
hostName = "192.168.4.254"
# Change this port number if needed
serverPort = 8080

class MyServer(BaseHTTPRequestHandler):
  def do_GET(self):

    randNum = randint(1,20)
    # If evenly divisble by 3, slow way down and recompute randNum
    if ( randNum % 3 == 0 ):
      time.sleep(randNum/10);
      randNum = randint(1,20)

    self.send_response(200)
    self.send_header("Content-type", "text/plain")
    self.end_headers()
    self.wfile.write(bytes(str(randNum), "utf-8"))


if __name__ == "__main__":        
  webServer = HTTPServer((hostName, serverPort), MyServer)
  print("Server started http://%s:%s" % (hostName, serverPort))

  try:
    webServer.serve_forever()
  except KeyboardInterrupt:
    pass

  webServer.server_close()
  print("Server stopped.")

# vim: ts=2 autoindent expandtab
