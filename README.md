looper-chat
=================================

looper-chat is an actor based chat system that supports concurrent WebSocket connections to the server with highly
available socket connections. Each WebSocket is mapped to an actor and the actors connect with bots that process the
messages and responds with default responses if required.

Following are the components:

API Actor Routes
=================================

Used to send chat messages for database backup. The API Actor is a silent actor and does what it's told without questions.
With a replication of 5 and connected with each other in a round robin pool.

Bot Actor Routes
=================================

Used to send chat messages to the artificial intelligence agent.