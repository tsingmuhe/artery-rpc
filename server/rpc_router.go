package server

import "context"

type router struct {
}

func newRpcRouter() Router {
	return &router{}
}

func (r router) ServeRequest(ctx context.Context, request Request, response Response) error {
	return nil
}
