package server

import (
	"github.com/tsingmuhe/artery-rpc/codec"
	"github.com/tsingmuhe/artery-rpc/service"
)

type Server interface {
	Handler(handler interface{}) error
	Run() error
	Stop() error
}

func NewRPCServer(opts ...Option) Server {
	return service.New(name, opts...)
}

type Handler interface {
	Name() string
	Handler() interface{}
}

type Request interface {
	Service() string
	Method() string
	Endpoint() string
	ContentType() string
	Header() map[string]string
	Body() interface{}
	Read() ([]byte, error)
	Codec() codec.Reader
	Stream() bool
}

type Response interface {
	Codec() codec.Writer
	WriteHeader(map[string]string)
	Write([]byte) error
}
