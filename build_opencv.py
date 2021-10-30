import pathlib
import sys
import sh

def clone_if_not_exists(name, url, **kwargs):
    if not pathlib.Path(name).exists():
        print("Cloning", url, "...")
        sh.git.clone(url, name, depth=1, **kwargs)

def build_opencv():
    sh.pip.install("numpy")
    clone_if_not_exists("opencv", "https://github.com/PolarNick239/opencv.git", branch="stable_3.0.0)
    clone_if_not_exists("opencv_contrib", "https://github.com/PolarNick239/opencv_contrib.git", branch="stable_3.0.0")
    sh.rm("-rf", "build")
    sh.mkdir("build")
    sh.cd("build")
    python_path = pathlib.Path(sh.pyenv.which("python").stdout.decode()).parent.parent
    version = "{}.{}".format(sys.version_info.major, sys.version_info.minor)
    sh.cmake(
        "..",
        "-DCMAKE_BUILD_TYPE=RELEASE",
        "-DCMAKE_INSTALL_PREFIX={}/usr/local".format(python_path),
        "-DWITH_CUDA=OFF",
        "-DWITH_FFMPEG=OFF",
        "-DINSTALL_C_EXAMPLES=OFF",
        "-DBUILD_opencv_legacy=OFF",
        "-DBUILD_NEW_PYTHON_SUPPORT=ON",
        "-DBUILD_opencv_python3=ON",
        "-DOPENCV_EXTRA_MODULES_PATH=~/opencv_contrib-4.3.2/modules",
        "-DBUILD_EXAMPLES=ON",
        "-DPYTHON_EXECUTABLE={}/bin/python".format(python_path),
        "-DPYTHON3_LIBRARY={}/lib/libpython{}m.so".format(python_path, version),
        "-DPYTHON3_PACKAGES_PATH={}/lib/python{}/site-packages/".format(python_path, version),
        "-DPYTHON3_NUMPY_INCLUDE_DIRS={}/lib/python{}/site-packages/numpy/core/include".format(python_path, version),
        "-DPYTHON_INCLUDE_DIR={}/include/python{}m".format(python_path, version),
        _out=sys.stdout,
    )
    sh.make("-j4", _out=sys.stdout)
    sh.make.install(_out=sys.stdout)


if __name__ == "__main__":
    build_opencv()
