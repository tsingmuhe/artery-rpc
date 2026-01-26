package server

var (
	DefaultServer = newRPCServer()
)

type Server interface {
	Handler(handler interface{}) error

	Run() error

	Stop() error
}
