package server

type rpcServer struct {
	opts Options

	handlers map[string]Handler
}

func (r rpcServer) Handler(handler interface{}) error {
	panic("implement me")
}

func (r rpcServer) Run() error {
	panic("implement me")
}

func (r rpcServer) Stop() error {
	panic("implement me")
}

func newRPCServer(opts ...Option) Server {
	return &rpcServer{
		opts:     newOptions(opts...),
		handlers: make(map[string]Handler),
	}
}
