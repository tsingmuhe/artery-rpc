package client

import "context"

var (
	DefaultClient = newRPCClient()
)

type Client interface {
	Call(ctx context.Context, req Request, rsp interface{}) error
}

type Request interface {
	Service() string
	Method() string
	Endpoint() string
	ContentType() string
	Body() interface{}
	Stream() bool
}
