package transport

import "net"

type Transport interface {
	Listen(addr net.Addr) (Listener, error)
	Dial(addr net.Addr) (Conn, error)
}

type Listener interface {
	Serve(func(Conn)) error
	Addr() string
	Close() error
}

type Message struct {
	Header map[string]string
	Body   []byte
}

type Conn interface {
	Send(*Message) error
	Recv(*Message) error
	Local() string
	Remote() string
	Close() error
}
