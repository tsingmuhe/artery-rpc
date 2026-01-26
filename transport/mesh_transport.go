package transport

type meshTransport struct {
}

func (u *meshTransport) Listen(addr string) (Listener, error) {
	panic("implement me")
}

func (u *meshTransport) Dial(addr string) (Socket, error) {
	panic("implement me")
}

func newMeshTransport() Transport {
	return &meshTransport{}
}
