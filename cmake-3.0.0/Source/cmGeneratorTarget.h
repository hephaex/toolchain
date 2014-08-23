/*============================================================================
  CMake - Cross Platform Makefile Generator
  Copyright 2000-2012 Kitware, Inc., Insight Software Consortium

  Distributed under the OSI-approved BSD License (the "License");
  see accompanying file Copyright.txt for details.

  This software is distributed WITHOUT ANY WARRANTY; without even the
  implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the License for more information.
============================================================================*/
#ifndef cmGeneratorTarget_h
#define cmGeneratorTarget_h

#include "cmStandardIncludes.h"

class cmCustomCommand;
class cmGlobalGenerator;
class cmLocalGenerator;
class cmMakefile;
class cmSourceFile;
class cmTarget;

class cmGeneratorTarget
{
public:
  cmGeneratorTarget(cmTarget*);

  int GetType() const;
  const char *GetName() const;
  const char *GetProperty(const char *prop) const;
  bool GetPropertyAsBool(const char *prop) const;
  void GetSourceFiles(std::vector<cmSourceFile*>& files) const;

  void GetObjectSources(std::vector<cmSourceFile*> &) const;
  const std::string& GetObjectName(cmSourceFile const* file);

  void AddObject(cmSourceFile *sf, std::string const&name);
  bool HasExplicitObjectName(cmSourceFile const* file) const;
  void AddExplicitObjectName(cmSourceFile* sf);

  void GetResxSources(std::vector<cmSourceFile*>&) const;
  void GetIDLSources(std::vector<cmSourceFile*>&) const;
  void GetExternalObjects(std::vector<cmSourceFile*>&) const;
  void GetHeaderSources(std::vector<cmSourceFile*>&) const;
  void GetExtraSources(std::vector<cmSourceFile*>&) const;
  void GetCustomCommands(std::vector<cmSourceFile*>&) const;
  void GetExpectedResxHeaders(std::set<std::string>&) const;

  cmTarget* Target;
  cmMakefile* Makefile;
  cmLocalGenerator* LocalGenerator;
  cmGlobalGenerator const* GlobalGenerator;

  std::string ModuleDefinitionFile;

  /** Full path with trailing slash to the top-level directory
      holding object files for this target.  Includes the build
      time config name placeholder if needed for the generator.  */
  std::string ObjectDirectory;

  void UseObjectLibraries(std::vector<std::string>& objs) const;

  void GetAppleArchs(const char* config,
                     std::vector<std::string>& archVec) const;

  ///! Return the rule variable used to create this type of target,
  //  need to add CMAKE_(LANG) for full name.
  const char* GetCreateRuleVariable() const;

  /** Get the include directories for this target.  */
  std::vector<std::string> GetIncludeDirectories(const char *config) const;

  bool IsSystemIncludeDirectory(const char *dir, const char *config) const;

  /** Add the target output files to the global generator manifest.  */
  void GenerateTargetManifest(const char* config) const;

  /**
   * Trace through the source files in this target and add al source files
   * that they depend on, used by all generators
   */
  void TraceDependencies();

  void ClassifySources();
  void LookupObjectLibraries();

  /** Get sources that must be built before the given source.  */
  std::vector<cmSourceFile*> const* GetSourceDepends(cmSourceFile* sf) const;

private:
  friend class cmTargetTraceDependencies;
  struct SourceEntry { std::vector<cmSourceFile*> Depends; };
  typedef std::map<cmSourceFile*, SourceEntry> SourceEntriesType;
  SourceEntriesType SourceEntries;

  std::vector<cmSourceFile*> CustomCommands;
  std::vector<cmSourceFile*> ExtraSources;
  std::vector<cmSourceFile*> HeaderSources;
  std::vector<cmSourceFile*> ExternalObjects;
  std::vector<cmSourceFile*> IDLSources;
  std::vector<cmSourceFile*> ResxSources;
  std::map<cmSourceFile const*, std::string> Objects;
  std::set<cmSourceFile const*> ExplicitObjectName;
  std::set<std::string> ExpectedResxHeaders;
  std::vector<cmSourceFile*> ObjectSources;
  std::vector<cmTarget*> ObjectLibraries;
  mutable std::map<std::string, std::vector<std::string> > SystemIncludesCache;

  cmGeneratorTarget(cmGeneratorTarget const&);
  void operator=(cmGeneratorTarget const&);
};

struct cmStrictTargetComparison {
  bool operator()(cmTarget const* t1, cmTarget const* t2) const;
};

typedef std::map<cmTarget const*,
                 cmGeneratorTarget*,
                 cmStrictTargetComparison> cmGeneratorTargetsType;

#endif
