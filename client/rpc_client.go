package client

import "context"

type rpcClient struct {
	opts Options
}

func (r rpcClient) Call(ctx context.Context, req Request, rsp interface{}) error {
	panic("implement me")
}

func newRPCClient(opt ...Option) Client {
	return &rpcClient{
		opts: newOptions(opt...),
	}
}
