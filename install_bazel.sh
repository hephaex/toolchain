INSTALL_DIR=$PWD
cd $HOME
wget --no-check-certificate https://github.com/bazelbuild/bazel/releases/download/0.11.1/bazel-0.11.1-dist.zip
unzip bazel-0.11.1-dist.zip -d bazel-0.11.1-dist
sudo chmod -R ug+rwx $HOME/bazel-0.11.1-dist
cd bazel-0.11.1-dist
./compile.sh
sudo cp output/bazel /usr/local/bin
