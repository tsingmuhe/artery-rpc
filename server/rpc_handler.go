package server

import "reflect"

type rpcHandler struct {
	name    string
	handler interface{}
}

func newRpcHandler(handler interface{}) Handler {
	hdlr := reflect.ValueOf(handler)
	name := reflect.Indirect(hdlr).Type().Name()

	return &rpcHandler{
		name:    name,
		handler: handler,
	}
}

func (r *rpcHandler) Name() string {
	return r.name
}

func (r *rpcHandler) Handler() interface{} {
	return r.handler
}
