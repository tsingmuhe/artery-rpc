package server

import (
	"sync"
	"time"

	"github.com/tsingmuhe/artery-rpc/transport"
)

type rpcServer struct {
	opts     Options
	handlers map[string]Handler

	router Router

	sync.RWMutex
	started bool
}

func newRPCServer(opts ...Option) Server {
	return &rpcServer{
		opts:     newOptions(opts...),
		handlers: make(map[string]Handler),
		router:   newRpcRouter(),
	}
}

func (s *rpcServer) Handler(h interface{}) error {
	s.Lock()
	defer s.Unlock()

	handler := newRpcHandler(h)

	err := s.router.Handle(handler)
	if err != nil {
		return err
	}

	s.handlers[handler.Name()] = handler
	return nil
}

func (s *rpcServer) Run() error {
	if s.isStarted() {
		return nil
	}

	opts := s.opts

	listener, err := opts.Transport.Listen(opts.Address)
	if err != nil {
		return err
	}

	go s.listen(listener)

	s.setStarted(true)
	return nil
}

func (s *rpcServer) listen(listener transport.Listener) {
	err := listener.Accept(s.handleConn)
}

func (s *rpcServer) handleConn(conn transport.Conn) {
	for {
		msg := &transport.Message{
			Header: make(map[string]string),
		}

		if err := conn.Recv(msg); err != nil {
			return
		}

	}
}

func (s *rpcServer) Stop() error {
	panic("implement me")
}

func (s *rpcServer) isStarted() bool {
	s.RLock()
	defer s.RUnlock()

	return s.started
}

func (s *rpcServer) setStarted(b bool) {
	s.Lock()
	defer s.Unlock()

	s.started = b
}
