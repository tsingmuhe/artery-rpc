package server

type Handler interface {
	Name() string
	Handler() interface{}
}
