package transport

type Options struct {
}

type Option func(*Options)

func newOptions(options ...Option) Options {
	opts := Options{}

	for _, o := range options {
		o(&opts)
	}

	return opts
}
