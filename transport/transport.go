package transport

var (
	DefaultTransport = newMeshTransport()
)

type Transport interface {
	Listen(addr string) (Listener, error)
	Dial(addr string) (Socket, error)
}

type Listener interface {
	Addr() string
	Close() error
	Accept(func(Socket)) error
}

type Message struct {
	Header map[string]string
	Body   []byte
}

type Socket interface {
	Send(*Message) error
	Recv(*Message) error
	Local() string
	Remote() string
	Close() error
}
