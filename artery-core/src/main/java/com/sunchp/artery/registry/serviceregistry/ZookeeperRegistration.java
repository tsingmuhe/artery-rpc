package com.sunchp.artery.registry.serviceregistry;

import com.sunchp.artery.registry.ZookeeperInstance;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.ServiceType;
import org.apache.curator.x.discovery.UriSpec;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

public class ZookeeperRegistration {
    public static RegistrationBuilder builder() {
        try {
            return new RegistrationBuilder(ServiceInstance.<ZookeeperInstance>builder());
        } catch (Exception e) {
            throw new RuntimeException("Error creating ServiceInstanceBuilder", e);
        }
    }

    public static RegistrationBuilder builder(ServiceInstanceBuilder<ZookeeperInstance> builder) {
        return new RegistrationBuilder(builder);
    }

    public static class RegistrationBuilder {
        protected ServiceInstanceBuilder<ZookeeperInstance> builder;

        public RegistrationBuilder(ServiceInstanceBuilder<ZookeeperInstance> builder) {
            this.builder = builder;
        }

        public ZookeeperRegistration build() {
            return new ZookeeperRegistration(this.builder);
        }

        public RegistrationBuilder name(String name) {
            this.builder.name(name);
            return this;
        }

        public RegistrationBuilder address(String address) {
            this.builder.address(address);
            return this;
        }

        public RegistrationBuilder id(String id) {
            this.builder.id(id);
            return this;
        }

        public RegistrationBuilder port(int port) {
            this.builder.port(port);
            return this;
        }

        public RegistrationBuilder sslPort(int port) {
            this.builder.sslPort(port);
            return this;
        }

        public RegistrationBuilder payload(ZookeeperInstance payload) {
            this.builder.payload(payload);
            return this;
        }

        public RegistrationBuilder serviceType(ServiceType serviceType) {
            this.builder.serviceType(serviceType);
            return this;
        }

        public RegistrationBuilder registrationTimeUTC(long registrationTimeUTC) {
            this.builder.registrationTimeUTC(registrationTimeUTC);
            return this;
        }

        public RegistrationBuilder uriSpec(UriSpec uriSpec) {
            this.builder.uriSpec(uriSpec);
            return this;
        }

        public RegistrationBuilder uriSpec(String uriSpec) {
            this.builder.uriSpec(new UriSpec(uriSpec));
            return this;
        }
    }

    protected ServiceInstance<ZookeeperInstance> serviceInstance;
    protected ServiceInstanceBuilder<ZookeeperInstance> builder;

    public ZookeeperRegistration(ServiceInstanceBuilder<ZookeeperInstance> builder) {
        this.builder = builder;
    }

    public ServiceInstance<ZookeeperInstance> getServiceInstance() {
        if (this.serviceInstance == null) {
            build();
        }
        return this.serviceInstance;
    }

    protected void build() {
        this.serviceInstance = this.builder.build();
    }

    public String getServiceId() {
        if (this.serviceInstance == null) {
            return null;
        }
        return this.serviceInstance.getName();
    }

    public int getPort() {
        if (this.serviceInstance == null) {
            return 0;
        }
        return this.serviceInstance.getPort();
    }

    public void setPort(int port) {
        this.builder.port(port);
        this.build();
    }

    public String getHost() {
        if (this.serviceInstance == null) {
            return null;
        }
        return this.serviceInstance.getAddress();
    }

    public boolean isSecure() {
        if (this.serviceInstance == null) {
            return false;
        }
        return this.serviceInstance.getSslPort() != null;
    }

    public URI getUri() {
        if (this.serviceInstance == null) {
            return null;
        }
        return URI.create(this.serviceInstance.buildUriSpec());
    }

    public Map<String, String> getMetadata() {
        if (this.serviceInstance == null || this.serviceInstance.getPayload() == null) {
            return Collections.emptyMap();
        }
        return this.serviceInstance.getPayload().getMetadata();
    }
}
