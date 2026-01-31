package transport

import (
	"bufio"
	"net"
)

type netListener struct {
	listener net.Listener
}

func (n *netListener) Accept(serveConn func(Conn)) error {
	for {
		conn, err := n.listener.Accept()
		if err != nil {
			return err
		}

		sc := netServerConn{
			conn: conn,
			r:    bufio.NewReader(conn),
			w:    bufio.NewWriter(conn),
		}

		go serveConn(sc)
	}
}

func (n *netListener) Addr() string {
	return n.listener.Addr().String()
}

func (n *netListener) Close() error {
	return n.listener.Close()
}
