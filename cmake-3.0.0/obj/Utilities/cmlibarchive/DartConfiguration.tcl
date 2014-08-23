# This file is configured by CMake automatically as DartConfiguration.tcl
# If you choose not to use CMake, this file may be hand configured, by
# filling in the required variables.


# Configuration directories and files
SourceDirectory: /Users/mscho/Simon/Tools/cmake-3.0.0/Utilities/cmlibarchive
BuildDirectory: /Users/mscho/Simon/Tools/cmake-3.0.0/obj/Utilities/cmlibarchive

# Where to place the cost data store
CostDataFile: 

# Site is something like machine.domain, i.e. pragmatic.crd
Site: Medardus.local

# Build name is osname-revision-compiler, i.e. Linux-2.4.2-2smp-c++
BuildName: Darwin-g++

# Submission information
IsCDash: TRUE
CDashVersion: 1.6
QueryCDashVersion: TRUE
DropSite: www.cdash.org
DropLocation: /CDash/submit.php?project=CMake
DropSiteUser: 
DropSitePassword: 
DropSiteMode: 
DropMethod: http
TriggerSite: http://public.kitware.com/cgi-bin/Submit-CMake-TestingResults.cgi
ScpCommand: /usr/bin/scp

# Dashboard start time
NightlyStartTime: 1:00:00 UTC

# Commands for the build/test/submit cycle
ConfigureCommand: "/Users/mscho/Simon/Tools/cmake-3.0.0/obj/Bootstrap.cmk/cmake" "/Users/mscho/Simon/Tools/cmake-3.0.0/Utilities/cmlibarchive"
MakeCommand: /Users/mscho/Simon/Tools/cmake-3.0.0/obj/Bootstrap.cmk/cmake --build . --config "${CTEST_CONFIGURATION_TYPE}" -- -i
DefaultCTestConfigurationType: Release

# CVS options
# Default is "-d -P -A"
CVSCommand: CVSCOMMAND-NOTFOUND
CVSUpdateOptions: -d -A -P

# Subversion options
SVNCommand: /usr/bin/svn
SVNOptions: 
SVNUpdateOptions: 

# Git options
GITCommand: /usr/local/bin/git
GITUpdateOptions: 
GITUpdateCustom: 

# Perforce options
P4Command: P4COMMAND-NOTFOUND
P4Client: 
P4Options: 
P4UpdateOptions: 
P4UpdateCustom: 

# Generic update command
UpdateCommand: 
UpdateOptions: 
UpdateType: 

# Compiler info
Compiler: /usr/local/bin/g++

# Dynamic analysis (MemCheck)
PurifyCommand: 
ValgrindCommand: 
ValgrindCommandOptions: 
MemoryCheckCommand: MEMORYCHECK_COMMAND-NOTFOUND
MemoryCheckCommandOptions: 
MemoryCheckSuppressionFile: 

# Coverage
CoverageCommand: /usr/local/bin/gcov
CoverageExtraFlags: -l

# Cluster commands
SlurmBatchCommand: SLURM_SBATCH_COMMAND-NOTFOUND
SlurmRunCommand: SLURM_SRUN_COMMAND-NOTFOUND

# Testing options
# TimeOut is the amount of time in seconds to wait for processes
# to complete during testing.  After TimeOut seconds, the
# process will be summarily terminated.
# Currently set to 25 minutes
TimeOut: 1500

UseLaunchers: 
CurlOptions: 
# warning, if you add new options here that have to do with submit,
# you have to update cmCTestSubmitCommand.cxx

# For CTest submissions that timeout, these options
# specify behavior for retrying the submission
CTestSubmitRetryDelay: 5
CTestSubmitRetryCount: 3
