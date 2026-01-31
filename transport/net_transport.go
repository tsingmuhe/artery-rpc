package transport

import (
	"net"
	"sync/atomic"
)

type netTransport struct {
	opts Options

	inShutdown atomic.Bool
}

func NewNetTransport(opts ...Option) Transport {
	return &netTransport{opts: newOptions(opts...)}
}

func (n *netTransport) Listen(addr net.Addr) (Listener, error) {
	listener, err := net.Listen(addr.Network(), addr.String())
	if err != nil {
		return nil, err
	}

	return &netListener{
		listener: listener,
	}, nil
}

func (n *netTransport) Dial(addr net.Addr) (Conn, error) {
	//TODO implement me
	panic("implement me")
}
