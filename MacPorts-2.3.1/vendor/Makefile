TCL_SUBDIR=			tcl8.5.15/unix
TCL_TARGETS=		install-binaries install-libraries

TCLTHREAD_SUBDIR=	thread2.7.0
TCLTREHAD_TARGETS=	install-binaries install-libraries

TCLLIB_SUBDIR=		tcllib-1.15
TCLLIB_TARGETS= 	install-libraries

DESTROOT=   		/Users/mscho/Simon/Tools/MacPorts-2.3.1/vendor/vendor-destroot

.PHONY: all clean distclean install destroot test
.PHONY: destroot-tcl destroot-tcllthread destroot-tcllib
.PHONY: install-tcl install-tcllthread install-tcllib
.PHONY: %-tcl %-tcllthread %-tcllib

# for make all, run destroot (where destroot will depend on all of each
# subpackage)
all: destroot

%-tcl:
	@echo ===\> making $(@:%-tcl=%) in ${DIRPRFX}tcl8.5.15/unix
	@umask 0022; $(MAKE) -C tcl8.5.15/unix $(@:%-tcl=%)

%-tclthread:
	@echo ===\> making $(@:%-tclthread=%) in ${DIRPRFX}thread2.7.0
	@umask 0022; $(MAKE) -C thread2.7.0 $(@:%-tclthread=%)

# thread links against libtclstub
all-tclthread: all-tcl

%-tcllib:
	@echo ===\> making $(@:%-tcllib=%) in ${DIRPRFX}tcllib-1.15
	@umask 0022; $(MAKE) -C tcllib-1.15 $(@:%-tcllib=%)

destroot: destroot-tcl destroot-tclthread destroot-tcllib
destroot-tcl: all-tcl
	@echo ===\> staging to destroot in ${DIRPRFX}tcl8.5.15/unix
	@umask 0022; $(MAKE) -C tcl8.5.15/unix DESTDIR=${DESTROOT} install-binaries install-libraries

destroot-tclthread: all-tclthread
	@echo ===\> staging to destroot in ${DIRPRFX}thread2.7.0
	@umask 0022; $(MAKE) -C thread2.7.0 DESTDIR=${DESTROOT} install-binaries install-libraries

destroot-tcllib: all-tcllib
	@echo ===\> staging to destroot in ${DIRPRFX}tcllib-1.15
	@umask 0022; $(MAKE) -C tcllib-1.15 DESTDIR=${DESTROOT} install-libraries

install: install-tcl install-tclthread install-tcllib
install-tcl:
	@echo ===\> making $(@:%-tcl=%) in ${DIRPRFX}tcl8.5.15/unix
	@umask 0022; $(MAKE) -C tcl8.5.15/unix install-binaries install-libraries

install-tclthread:
	@echo ===\> making $(@:%-tclthrad=%) in ${DIRPRFX}thread2.7.0
	@umask 0022; $(MAKE) -C thread2.7.0 install-binaries install-libraries

install-tcllib:
	@echo ===\> making $(@:%-tcllib=%) in ${DIRPRFX}tcllib-1.15
	@umask 0022; $(MAKE) -C tcllib-1.15 install-libraries

test:

clean: clean-tcl clean-tcllib clean-tclthread
	rm -rf ${DESTROOT}

distclean:
	rm -f Makefile
	rm -rf $(TCL_SUBDIR:/unix=) $(TCLTHREAD_SUBDIR) $(TCLLIB_SUBDIR)
