package transport

import (
	"bufio"
	"net"
)

type netServerConn struct {
	conn net.Conn
	r    *bufio.Reader
	w    *bufio.Writer
}

func (n netServerConn) Send(message *Message) error {
	//TODO implement me
	panic("implement me")
}

func (n netServerConn) Recv(message *Message) error {
	//TODO implement me
	panic("implement me")
}

func (n netServerConn) Local() string {
	//TODO implement me
	panic("implement me")
}

func (n netServerConn) Remote() string {
	//TODO implement me
	panic("implement me")
}

func (n netServerConn) Close() error {
	//TODO implement me
	panic("implement me")
}
