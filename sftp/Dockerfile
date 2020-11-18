FROM centos
MAINTAINER Sergei Gribanov

RUN yum -y install openssh-server

RUN mkdir -p /var/run/sshd
RUN groupadd --system sftp

ADD . /root
WORKDIR /root
RUN mv sshd_config /etc/ssh/sshd_config
RUN ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key

EXPOSE 22

RUN useradd -ms /bin/bash foo
RUN echo "foo:123" | chpasswd
USER foo
USER 0
RUN echo "root:root" | chpasswd
RUN chown root:root /home/foo
RUN chmod 755 /home/foo

CMD ["/usr/sbin/sshd", "-D"]
