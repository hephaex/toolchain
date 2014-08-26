/* A Bison parser, made by GNU Bison 2.3.  */

/* Skeleton implementation for Bison's Yacc-like parsers in C

   Copyright (C) 1984, 1989, 1990, 2000, 2001, 2002, 2003, 2004, 2005, 2006
   Free Software Foundation, Inc.

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2, or (at your option)
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor,
   Boston, MA 02110-1301, USA.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* C LALR(1) parser skeleton written by Richard Stallman, by
   simplifying the original so-called "semantic" parser.  */

/* All symbols defined below should begin with yy or YY, to avoid
   infringing on user name space.  This should be done even for local
   variables, as they might otherwise be expanded by user macros.
   There are some unavoidable exceptions within include files to
   define necessary library symbols; they are noted "INFRINGES ON
   USER NAME SPACE" below.  */

/* Identify Bison output.  */
#define YYBISON 1

/* Bison version.  */
#define YYBISON_VERSION "2.3"

/* Skeleton name.  */
#define YYSKELETON_NAME "yacc.c"

/* Pure parsers.  */
#define YYPURE 0

/* Using locations.  */
#define YYLSP_NEEDED 0

/* Substitute the variable and function names.  */
#define yyparse vhdlscannerYYparse
#define yylex   vhdlscannerYYlex
#define yyerror vhdlscannerYYerror
#define yylval  vhdlscannerYYlval
#define yychar  vhdlscannerYYchar
#define yydebug vhdlscannerYYdebug
#define yynerrs vhdlscannerYYnerrs


/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     t_ABSTRLIST = 258,
     t_CHARLIST = 259,
     t_DIGIT = 260,
     t_STRING = 261,
     t_LETTER = 262,
     t_ACCESS = 263,
     t_AFTER = 264,
     t_ALIAS = 265,
     t_ALL = 266,
     t_AND = 267,
     t_ARCHITECTURE = 268,
     t_ARRAY = 269,
     t_ASSERT = 270,
     t_ATTRIBUTE = 271,
     t_BEGIN = 272,
     t_BLOCK = 273,
     t_BODY = 274,
     t_BUFFER = 275,
     t_BUS = 276,
     t_CASE = 277,
     t_COMPONENT = 278,
     t_CONFIGURATION = 279,
     t_CONSTANT = 280,
     t_DISCONNECT = 281,
     t_DOWNTO = 282,
     t_ELSE = 283,
     t_ELSIF = 284,
     t_END = 285,
     t_ENTITY = 286,
     t_EXIT = 287,
     t_FILE = 288,
     t_FOR = 289,
     t_FUNCTION = 290,
     t_GENERATE = 291,
     t_GENERIC = 292,
     t_GUARDED = 293,
     t_IF = 294,
     t_IN = 295,
     t_INOUT = 296,
     t_IS = 297,
     t_LABEL = 298,
     t_LIBRARY = 299,
     t_LINKAGE = 300,
     t_LOOP = 301,
     t_MAP = 302,
     t_NAND = 303,
     t_NEW = 304,
     t_NEXT = 305,
     t_NOR = 306,
     t_NULL = 307,
     t_OF = 308,
     t_ON = 309,
     t_OPEN = 310,
     t_OR = 311,
     t_OTHERS = 312,
     t_OUT = 313,
     t_PACKAGE = 314,
     t_PORT = 315,
     t_PROCEDURE = 316,
     t_PROCESS = 317,
     t_RANGE = 318,
     t_RECORD = 319,
     t_REGISTER = 320,
     t_REPORT = 321,
     t_RETURN = 322,
     t_SELECT = 323,
     t_SEVERITY = 324,
     t_SIGNAL = 325,
     t_SUBTYPE = 326,
     t_THEN = 327,
     t_TO = 328,
     t_TRANSPORT = 329,
     t_TYPE = 330,
     t_UNITS = 331,
     t_UNTIL = 332,
     t_USE = 333,
     t_VARIABLE = 334,
     t_WAIT = 335,
     t_WHEN = 336,
     t_WHILE = 337,
     t_WITH = 338,
     t_XOR = 339,
     t_IMPURE = 340,
     t_PURE = 341,
     t_GROUP = 342,
     t_POSTPONED = 343,
     t_SHARED = 344,
     t_XNOR = 345,
     t_SLL = 346,
     t_SRA = 347,
     t_SLA = 348,
     t_SRL = 349,
     t_ROR = 350,
     t_ROL = 351,
     t_UNAFFECTED = 352,
     t_ASSUME_GUARANTEE = 353,
     t_ASSUME = 354,
     t_CONTEXT = 355,
     t_COVER = 356,
     t_DEFAULT = 357,
     t_FAIRNESS = 358,
     t_FORCE = 359,
     t_INERTIAL = 360,
     t_LITERAL = 361,
     t_PARAMETER = 362,
     t_PROTECTED = 363,
     t_PROPERTY = 364,
     t_REJECT = 365,
     t_RELEASE = 366,
     t_RESTRICT = 367,
     t_RESTRICT_GUARANTEE = 368,
     t_SEQUENCE = 369,
     t_STRONG = 370,
     t_VMODE = 371,
     t_VPROP = 372,
     t_VUNIT = 373,
     t_SLSL = 374,
     t_SRSR = 375,
     t_QQ = 376,
     t_QGT = 377,
     t_QLT = 378,
     t_QG = 379,
     t_QL = 380,
     t_QEQU = 381,
     t_QNEQU = 382,
     t_GESym = 383,
     t_GTSym = 384,
     t_LESym = 385,
     t_LTSym = 386,
     t_NESym = 387,
     t_EQSym = 388,
     t_Ampersand = 389,
     t_Minus = 390,
     t_Plus = 391,
     MED_PRECEDENCE = 392,
     t_REM = 393,
     t_MOD = 394,
     t_Slash = 395,
     t_Star = 396,
     MAX_PRECEDENCE = 397,
     t_NOT = 398,
     t_ABS = 399,
     t_DoubleStar = 400,
     t_Apostrophe = 401,
     t_LeftParen = 402,
     t_RightParen = 403,
     t_Comma = 404,
     t_VarAsgn = 405,
     t_Colon = 406,
     t_Semicolon = 407,
     t_Arrow = 408,
     t_Box = 409,
     t_Bar = 410,
     t_Dot = 411,
     t_Q = 412,
     t_At = 413,
     t_Neg = 414,
     t_LEFTBR = 415,
     t_RIGHTBR = 416,
     t_ToolDir = 417
   };
#endif
/* Tokens.  */
#define t_ABSTRLIST 258
#define t_CHARLIST 259
#define t_DIGIT 260
#define t_STRING 261
#define t_LETTER 262
#define t_ACCESS 263
#define t_AFTER 264
#define t_ALIAS 265
#define t_ALL 266
#define t_AND 267
#define t_ARCHITECTURE 268
#define t_ARRAY 269
#define t_ASSERT 270
#define t_ATTRIBUTE 271
#define t_BEGIN 272
#define t_BLOCK 273
#define t_BODY 274
#define t_BUFFER 275
#define t_BUS 276
#define t_CASE 277
#define t_COMPONENT 278
#define t_CONFIGURATION 279
#define t_CONSTANT 280
#define t_DISCONNECT 281
#define t_DOWNTO 282
#define t_ELSE 283
#define t_ELSIF 284
#define t_END 285
#define t_ENTITY 286
#define t_EXIT 287
#define t_FILE 288
#define t_FOR 289
#define t_FUNCTION 290
#define t_GENERATE 291
#define t_GENERIC 292
#define t_GUARDED 293
#define t_IF 294
#define t_IN 295
#define t_INOUT 296
#define t_IS 297
#define t_LABEL 298
#define t_LIBRARY 299
#define t_LINKAGE 300
#define t_LOOP 301
#define t_MAP 302
#define t_NAND 303
#define t_NEW 304
#define t_NEXT 305
#define t_NOR 306
#define t_NULL 307
#define t_OF 308
#define t_ON 309
#define t_OPEN 310
#define t_OR 311
#define t_OTHERS 312
#define t_OUT 313
#define t_PACKAGE 314
#define t_PORT 315
#define t_PROCEDURE 316
#define t_PROCESS 317
#define t_RANGE 318
#define t_RECORD 319
#define t_REGISTER 320
#define t_REPORT 321
#define t_RETURN 322
#define t_SELECT 323
#define t_SEVERITY 324
#define t_SIGNAL 325
#define t_SUBTYPE 326
#define t_THEN 327
#define t_TO 328
#define t_TRANSPORT 329
#define t_TYPE 330
#define t_UNITS 331
#define t_UNTIL 332
#define t_USE 333
#define t_VARIABLE 334
#define t_WAIT 335
#define t_WHEN 336
#define t_WHILE 337
#define t_WITH 338
#define t_XOR 339
#define t_IMPURE 340
#define t_PURE 341
#define t_GROUP 342
#define t_POSTPONED 343
#define t_SHARED 344
#define t_XNOR 345
#define t_SLL 346
#define t_SRA 347
#define t_SLA 348
#define t_SRL 349
#define t_ROR 350
#define t_ROL 351
#define t_UNAFFECTED 352
#define t_ASSUME_GUARANTEE 353
#define t_ASSUME 354
#define t_CONTEXT 355
#define t_COVER 356
#define t_DEFAULT 357
#define t_FAIRNESS 358
#define t_FORCE 359
#define t_INERTIAL 360
#define t_LITERAL 361
#define t_PARAMETER 362
#define t_PROTECTED 363
#define t_PROPERTY 364
#define t_REJECT 365
#define t_RELEASE 366
#define t_RESTRICT 367
#define t_RESTRICT_GUARANTEE 368
#define t_SEQUENCE 369
#define t_STRONG 370
#define t_VMODE 371
#define t_VPROP 372
#define t_VUNIT 373
#define t_SLSL 374
#define t_SRSR 375
#define t_QQ 376
#define t_QGT 377
#define t_QLT 378
#define t_QG 379
#define t_QL 380
#define t_QEQU 381
#define t_QNEQU 382
#define t_GESym 383
#define t_GTSym 384
#define t_LESym 385
#define t_LTSym 386
#define t_NESym 387
#define t_EQSym 388
#define t_Ampersand 389
#define t_Minus 390
#define t_Plus 391
#define MED_PRECEDENCE 392
#define t_REM 393
#define t_MOD 394
#define t_Slash 395
#define t_Star 396
#define MAX_PRECEDENCE 397
#define t_NOT 398
#define t_ABS 399
#define t_DoubleStar 400
#define t_Apostrophe 401
#define t_LeftParen 402
#define t_RightParen 403
#define t_Comma 404
#define t_VarAsgn 405
#define t_Colon 406
#define t_Semicolon 407
#define t_Arrow 408
#define t_Box 409
#define t_Bar 410
#define t_Dot 411
#define t_Q 412
#define t_At 413
#define t_Neg 414
#define t_LEFTBR 415
#define t_RIGHTBR 416
#define t_ToolDir 417




/* Copy the first part of user declarations.  */


#include <stdio.h>
#include <qcstring.h>
#include <qstringlist.h>
#include "config.h"

#ifndef YYSTYPE
typedef int YYSTYPE;
#endif

struct  YYMM
{
  int itype;
  QCString qstr;
};

 


// define struct instead of union
#define YYSTYPE YYMM

#include "membergroup.h"
#include "vhdldocgen.h"
#include "doxygen.h"
#include "searchindex.h"
#include "vhdlscanner.h"
#include "commentscan.h"
#include "entry.h"
#include "arguments.h"
#include "memberdef.h"
#include "vhdldocgen.h"

//-----------------------------variables ---------------------------------------------------------------------------


    


static VhdlContainer s_str;

static QList<Entry>instFiles;
static QList<Entry>libUse;


static int yyLineNr;
static Entry* lastCompound;
static Entry* currentCompound;
static Entry* lastEntity;
static Entry* current;
static Entry* tempEntry;
static Entry* current_root;
static QCString compSpec;
static QCString currName;
static int levelCounter;
static QCString confName;
static QCString genLabels;
static QCString lab;
static QCString forL;
static QList<VhdlConfNode> configL;


static int currP=0;

enum  { GEN_SEC=0x1, PARAM_SEC,CONTEXT_SEC,PROTECTED_SEC } ;

static int param_sec = 0;
static int parse_sec=0;


//---------------------------- functions --------------------------------------------------------------------------------

int vhdlscannerYYlex ();
void vhdlscannerYYerror (char const *);

static void addVhdlType(const QCString &name,int startLine,
                        int section,uint64 spec,
			const char* args,const char* type,
			Protection prot=Public);
static void addCompInst(char *n, char* instName,char* comp,int line);

static void newEntry();
static void initEntry(Entry *e);


static void pushLabel(QCString &,QCString&);
static QCString popLabel(QCString&);
static void addConfigureNode(const char* a,const char*b,
                         bool isRoot,bool isLeave,bool inlineConf=FALSE);
//static bool addLibUseClause(const QCString &type);
static bool isFuncProcProced();
static void initEntry(Entry *e);
static void addProto(const char *s1,const char *s2,const char *s3,
                     const char *s4,const char *s5,const char *s6);
static void createFunction(const QCString &impure,uint64 spec,
                           const QCString &fname);

static void createFlow();    

void newVhdlEntry()
{
  newEntry();
}

Entry* getCurrentVhdlEntry()
{
  return current;
}

void initVhdlParser()
{
  lastCompound=0;
  lastEntity=0;
  currentCompound=0;
  lastEntity=0;
  current_root=s_str.root;
  current=new Entry();
  initEntry(current);
  libUse.clear();
}

QList<Entry> & getVhdlInstList()
{
  return instFiles;
}

QList<Entry> & getLibUse()
{
  return libUse;
}



/* Enabling traces.  */
#ifndef YYDEBUG
# define YYDEBUG 1
#endif

/* Enabling verbose error messages.  */
#ifdef YYERROR_VERBOSE
# undef YYERROR_VERBOSE
# define YYERROR_VERBOSE 1
#else
# define YYERROR_VERBOSE 0
#endif

/* Enabling the token table.  */
#ifndef YYTOKEN_TABLE
# define YYTOKEN_TABLE 0
#endif

#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
# define YYSTYPE_IS_TRIVIAL 1
#endif



/* Copy the second part of user declarations.  */


/* Line 216 of yacc.c.  */


#ifdef short
# undef short
#endif

#ifdef YYTYPE_UINT8
typedef YYTYPE_UINT8 yytype_uint8;
#else
typedef unsigned char yytype_uint8;
#endif

#ifdef YYTYPE_INT8
typedef YYTYPE_INT8 yytype_int8;
#elif (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
typedef signed char yytype_int8;
#else
typedef short int yytype_int8;
#endif

#ifdef YYTYPE_UINT16
typedef YYTYPE_UINT16 yytype_uint16;
#else
typedef unsigned short int yytype_uint16;
#endif

#ifdef YYTYPE_INT16
typedef YYTYPE_INT16 yytype_int16;
#else
typedef short int yytype_int16;
#endif

#ifndef YYSIZE_T
# ifdef __SIZE_TYPE__
#  define YYSIZE_T __SIZE_TYPE__
# elif defined size_t
#  define YYSIZE_T size_t
# elif ! defined YYSIZE_T && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
#  include <stddef.h> /* INFRINGES ON USER NAME SPACE */
#  define YYSIZE_T size_t
# else
#  define YYSIZE_T unsigned int
# endif
#endif

#define YYSIZE_MAXIMUM ((YYSIZE_T) -1)

#ifndef YY_
# if defined YYENABLE_NLS && YYENABLE_NLS
#  if ENABLE_NLS
#   include <libintl.h> /* INFRINGES ON USER NAME SPACE */
#   define YY_(msgid) dgettext ("bison-runtime", msgid)
#  endif
# endif
# ifndef YY_
#  define YY_(msgid) msgid
# endif
#endif

/* Suppress unused-variable warnings by "using" E.  */
#if ! defined lint || defined __GNUC__
# define YYUSE(e) ((void) (e))
#else
# define YYUSE(e) /* empty */
#endif

/* Identity function, used to suppress warnings about constant conditions.  */
#ifndef lint
# define YYID(n) (n)
#else
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static int
YYID (int i)
#else
static int
YYID (i)
    int i;
#endif
{
  return i;
}
#endif

#if ! defined yyoverflow || YYERROR_VERBOSE

/* The parser invokes alloca or malloc; define the necessary symbols.  */

# ifdef YYSTACK_USE_ALLOCA
#  if YYSTACK_USE_ALLOCA
#   ifdef __GNUC__
#    define YYSTACK_ALLOC __builtin_alloca
#   elif defined __BUILTIN_VA_ARG_INCR
#    include <alloca.h> /* INFRINGES ON USER NAME SPACE */
#   elif defined _AIX
#    define YYSTACK_ALLOC __alloca
#   elif defined _MSC_VER
#    include <malloc.h> /* INFRINGES ON USER NAME SPACE */
#    define alloca _alloca
#   else
#    define YYSTACK_ALLOC alloca
#    if ! defined _ALLOCA_H && ! defined _STDLIB_H && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
#     include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#     ifndef _STDLIB_H
#      define _STDLIB_H 1
#     endif
#    endif
#   endif
#  endif
# endif

# ifdef YYSTACK_ALLOC
   /* Pacify GCC's `empty if-body' warning.  */
#  define YYSTACK_FREE(Ptr) do { /* empty */; } while (YYID (0))
#  ifndef YYSTACK_ALLOC_MAXIMUM
    /* The OS might guarantee only one guard page at the bottom of the stack,
       and a page size can be as small as 4096 bytes.  So we cannot safely
       invoke alloca (N) if N exceeds 4096.  Use a slightly smaller number
       to allow for a few compiler-allocated temporary stack slots.  */
#   define YYSTACK_ALLOC_MAXIMUM 4032 /* reasonable circa 2006 */
#  endif
# else
#  define YYSTACK_ALLOC YYMALLOC
#  define YYSTACK_FREE YYFREE
#  ifndef YYSTACK_ALLOC_MAXIMUM
#   define YYSTACK_ALLOC_MAXIMUM YYSIZE_MAXIMUM
#  endif
#  if (defined __cplusplus && ! defined _STDLIB_H \
       && ! ((defined YYMALLOC || defined malloc) \
	     && (defined YYFREE || defined free)))
#   include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#   ifndef _STDLIB_H
#    define _STDLIB_H 1
#   endif
#  endif
#  ifndef YYMALLOC
#   define YYMALLOC malloc
#   if ! defined malloc && ! defined _STDLIB_H && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
void *malloc (YYSIZE_T); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
#  ifndef YYFREE
#   define YYFREE free
#   if ! defined free && ! defined _STDLIB_H && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
void free (void *); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
# endif
#endif /* ! defined yyoverflow || YYERROR_VERBOSE */


#if (! defined yyoverflow \
     && (! defined __cplusplus \
	 || (defined YYSTYPE_IS_TRIVIAL && YYSTYPE_IS_TRIVIAL)))

/* A type that is properly aligned for any stack member.  */
union yyalloc
{
  yytype_int16 yyss;
  YYSTYPE yyvs;
  };

/* The size of the maximum gap between one aligned stack and the next.  */
# define YYSTACK_GAP_MAXIMUM (sizeof (union yyalloc) - 1)

/* The size of an array large to enough to hold all stacks, each with
   N elements.  */
# define YYSTACK_BYTES(N) \
     ((N) * (sizeof (yytype_int16) + sizeof (YYSTYPE)) \
      + YYSTACK_GAP_MAXIMUM)

/* Copy COUNT objects from FROM to TO.  The source and destination do
   not overlap.  */
# ifndef YYCOPY
#  if defined __GNUC__ && 1 < __GNUC__
#   define YYCOPY(To, From, Count) \
      __builtin_memcpy (To, From, (Count) * sizeof (*(From)))
#  else
#   define YYCOPY(To, From, Count)		\
      do					\
	{					\
	  YYSIZE_T yyi;				\
	  for (yyi = 0; yyi < (Count); yyi++)	\
	    (To)[yyi] = (From)[yyi];		\
	}					\
      while (YYID (0))
#  endif
# endif

/* Relocate STACK from its old location to the new one.  The
   local variables YYSIZE and YYSTACKSIZE give the old and new number of
   elements in the stack, and YYPTR gives the new location of the
   stack.  Advance YYPTR to a properly aligned location for the next
   stack.  */
# define YYSTACK_RELOCATE(Stack)					\
    do									\
      {									\
	YYSIZE_T yynewbytes;						\
	YYCOPY (&yyptr->Stack, Stack, yysize);				\
	Stack = &yyptr->Stack;						\
	yynewbytes = yystacksize * sizeof (*Stack) + YYSTACK_GAP_MAXIMUM; \
	yyptr += yynewbytes / sizeof (*yyptr);				\
      }									\
    while (YYID (0))

#endif

/* YYFINAL -- State number of the termination state.  */
#define YYFINAL  26
/* YYLAST -- Last index in YYTABLE.  */
#define YYLAST   2768

/* YYNTOKENS -- Number of terminals.  */
#define YYNTOKENS  163
/* YYNNTS -- Number of nonterminals.  */
#define YYNNTS  409
/* YYNRULES -- Number of rules.  */
#define YYNRULES  928
/* YYNRULES -- Number of states.  */
#define YYNSTATES  1598

/* YYTRANSLATE(YYLEX) -- Bison symbol number corresponding to YYLEX.  */
#define YYUNDEFTOK  2
#define YYMAXUTOK   417

#define YYTRANSLATE(YYX)						\
  ((unsigned int) (YYX) <= YYMAXUTOK ? yytranslate[YYX] : YYUNDEFTOK)

/* YYTRANSLATE[YYLEX] -- Bison symbol number corresponding to YYLEX.  */
static const yytype_uint8 yytranslate[] =
{
       0,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     1,     2,     3,     4,
       5,     6,     7,     8,     9,    10,    11,    12,    13,    14,
      15,    16,    17,    18,    19,    20,    21,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,    34,
      35,    36,    37,    38,    39,    40,    41,    42,    43,    44,
      45,    46,    47,    48,    49,    50,    51,    52,    53,    54,
      55,    56,    57,    58,    59,    60,    61,    62,    63,    64,
      65,    66,    67,    68,    69,    70,    71,    72,    73,    74,
      75,    76,    77,    78,    79,    80,    81,    82,    83,    84,
      85,    86,    87,    88,    89,    90,    91,    92,    93,    94,
      95,    96,    97,    98,    99,   100,   101,   102,   103,   104,
     105,   106,   107,   108,   109,   110,   111,   112,   113,   114,
     115,   116,   117,   118,   119,   120,   121,   122,   123,   124,
     125,   126,   127,   128,   129,   130,   131,   132,   133,   134,
     135,   136,   137,   138,   139,   140,   141,   142,   143,   144,
     145,   146,   147,   148,   149,   150,   151,   152,   153,   154,
     155,   156,   157,   158,   159,   160,   161,   162
};

#if YYDEBUG
/* YYPRHS[YYN] -- Index of the first RHS symbol of rule number YYN in
   YYRHS.  */
static const yytype_uint16 yyprhs[] =
{
       0,     0,     3,     5,     7,     9,    11,    13,    16,    18,
      20,    22,    24,    26,    28,    30,    32,    34,    37,    38,
      40,    43,    45,    49,    52,    53,    56,    58,    60,    62,
      64,    66,    68,    70,    72,    74,    78,    82,    84,    88,
      93,   101,   105,   106,   108,   109,   112,   113,   116,   118,
     119,   120,   125,   126,   127,   132,   136,   144,   150,   156,
     157,   159,   162,   164,   165,   168,   170,   176,   177,   185,
     191,   192,   194,   196,   199,   200,   203,   205,   211,   217,
     221,   222,   224,   227,   229,   230,   232,   235,   237,   240,
     242,   245,   249,   255,   261,   266,   267,   269,   272,   276,
     277,   280,   282,   284,   286,   288,   290,   292,   294,   296,
     298,   300,   302,   304,   306,   308,   310,   312,   314,   316,
     318,   320,   322,   324,   326,   328,   330,   332,   334,   336,
     338,   340,   342,   344,   346,   348,   350,   352,   355,   357,
     359,   361,   363,   365,   367,   369,   371,   373,   375,   377,
     379,   381,   383,   385,   387,   389,   391,   393,   395,   397,
     399,   401,   403,   405,   407,   409,   411,   413,   415,   417,
     419,   424,   426,   428,   431,   432,   437,   438,   446,   447,
     454,   456,   459,   460,   464,   465,   468,   469,   472,   474,
     476,   478,   479,   489,   496,   497,   499,   501,   503,   506,
     509,   512,   513,   516,   518,   523,   527,   528,   531,   534,
     536,   538,   541,   549,   550,   553,   554,   556,   558,   559,
     561,   562,   564,   566,   568,   570,   572,   574,   579,   580,
     583,   586,   589,   591,   596,   600,   604,   605,   608,   611,
     615,   617,   619,   621,   623,   627,   629,   631,   633,   635,
     638,   640,   642,   644,   646,   648,   650,   652,   654,   658,
     662,   666,   670,   674,   678,   682,   686,   690,   694,   698,
     702,   706,   709,   711,   714,   717,   720,   723,   727,   732,
     736,   740,   744,   748,   752,   756,   760,   764,   768,   772,
     776,   780,   784,   788,   792,   796,   800,   804,   808,   811,
     814,   816,   820,   822,   824,   826,   828,   832,   834,   836,
     838,   840,   842,   845,   848,   852,   854,   856,   858,   860,
     862,   866,   868,   870,   872,   874,   876,   878,   880,   882,
     886,   888,   890,   892,   895,   898,   900,   904,   909,   913,
     917,   921,   924,   930,   935,   939,   945,   949,   954,   958,
     961,   962,   964,   965,   967,   971,   973,   976,   977,   980,
     983,   985,   987,   989,   994,   999,  1003,  1004,  1007,  1009,
    1011,  1013,  1015,  1017,  1019,  1021,  1023,  1025,  1027,  1032,
    1033,  1036,  1039,  1046,  1049,  1051,  1052,  1055,  1057,  1060,
    1065,  1073,  1074,  1077,  1080,  1084,  1089,  1090,  1092,  1099,
    1100,  1103,  1105,  1110,  1113,  1117,  1123,  1127,  1130,  1132,
    1133,  1135,  1139,  1142,  1146,  1147,  1149,  1152,  1157,  1158,
    1161,  1164,  1166,  1168,  1170,  1174,  1176,  1180,  1182,  1184,
    1191,  1192,  1195,  1203,  1204,  1207,  1208,  1210,  1217,  1225,
    1226,  1229,  1231,  1233,  1235,  1238,  1240,  1242,  1244,  1246,
    1254,  1261,  1263,  1265,  1266,  1269,  1278,  1285,  1286,  1291,
    1292,  1294,  1302,  1305,  1307,  1309,  1310,  1313,  1316,  1322,
    1330,  1335,  1338,  1340,  1342,  1343,  1346,  1349,  1351,  1353,
    1355,  1357,  1359,  1361,  1363,  1365,  1367,  1369,  1371,  1373,
    1375,  1377,  1379,  1381,  1383,  1385,  1387,  1389,  1396,  1404,
    1405,  1409,  1414,  1415,  1418,  1423,  1429,  1431,  1433,  1435,
    1440,  1446,  1449,  1451,  1452,  1455,  1457,  1459,  1461,  1463,
    1465,  1467,  1469,  1471,  1472,  1488,  1489,  1491,  1492,  1495,
    1497,  1498,  1503,  1508,  1509,  1514,  1515,  1520,  1521,  1526,
    1527,  1529,  1531,  1535,  1538,  1540,  1544,  1546,  1548,  1550,
    1551,  1561,  1562,  1571,  1580,  1590,  1591,  1595,  1599,  1601,
    1606,  1609,  1613,  1615,  1620,  1623,  1627,  1629,  1634,  1637,
    1642,  1645,  1649,  1651,  1657,  1659,  1663,  1669,  1672,  1674,
    1675,  1678,  1681,  1684,  1685,  1688,  1691,  1693,  1696,  1698,
    1700,  1703,  1704,  1706,  1710,  1712,  1713,  1715,  1724,  1729,
    1730,  1733,  1738,  1739,  1742,  1744,  1747,  1748,  1757,  1762,
    1766,  1767,  1773,  1777,  1779,  1782,  1785,  1789,  1793,  1795,
    1796,  1797,  1809,  1814,  1815,  1819,  1820,  1822,  1823,  1825,
    1826,  1828,  1831,  1833,  1834,  1838,  1842,  1845,  1846,  1849,
    1852,  1854,  1855,  1858,  1860,  1862,  1865,  1867,  1869,  1871,
    1873,  1875,  1877,  1879,  1881,  1884,  1886,  1888,  1890,  1893,
    1895,  1901,  1907,  1908,  1911,  1912,  1915,  1916,  1918,  1919,
    1921,  1923,  1924,  1936,  1937,  1950,  1957,  1958,  1961,  1963,
    1964,  1970,  1971,  1982,  1983,  1984,  1988,  1989,  1992,  1993,
    1999,  2008,  2009,  2011,  2012,  2014,  2015,  2018,  2024,  2025,
    2028,  2029,  2031,  2037,  2038,  2041,  2042,  2044,  2047,  2050,
    2054,  2055,  2057,  2062,  2068,  2075,  2081,  2083,  2085,  2088,
    2090,  2093,  2095,  2098,  2102,  2107,  2113,  2114,  2117,  2118,
    2121,  2122,  2125,  2127,  2131,  2135,  2139,  2142,  2143,  2145,
    2153,  2154,  2158,  2159,  2163,  2164,  2173,  2179,  2180,  2183,
    2185,  2186,  2189,  2191,  2193,  2195,  2197,  2205,  2206,  2208,
    2209,  2213,  2218,  2222,  2227,  2235,  2238,  2243,  2246,  2250,
    2252,  2254,  2256,  2260,  2261,  2265,  2266,  2270,  2273,  2276,
    2278,  2280,  2282,  2284,  2288,  2297,  2305,  2313,  2316,  2317,
    2319,  2321,  2325,  2327,  2329,  2331,  2333,  2335,  2337,  2339,
    2344,  2349,  2350,  2353,  2355,  2357,  2360,  2362,  2364,  2366,
    2368,  2374,  2380,  2381,  2384,  2386,  2389,  2393,  2395,  2399,
    2400,  2409,  2416,  2418,  2421,  2423,  2426,  2428,  2430,  2432,
    2440,  2449,  2456,  2464,  2473,  2480,  2481,  2485,  2488,  2491,
    2493,  2497,  2499,  2502,  2505,  2515,  2524,  2533,  2539,  2544,
    2547,  2549,  2552,  2556,  2558,  2560,  2568,  2577,  2584,  2592,
    2597,  2602,  2605,  2615,  2624,  2626,  2628,  2637,  2638,  2640,
    2646,  2648,  2653,  2663,  2664,  2666,  2668,  2670,  2674,  2676,
    2682,  2687,  2692,  2698,  2701,  2709,  2715,  2717,  2722,  2724,
    2726,  2728,  2730,  2734,  2741,  2749,  2751,  2753,  2754,  2757,
    2760,  2761,  2763,  2764,  2771,  2776,  2782,  2789,  2793,  2794,
    2798,  2805,  2807,  2809,  2811,  2813,  2815,  2817,  2821,  2824,
    2828,  2831,  2834,  2838,  2840,  2845,  2848,  2852,  2855
};

/* YYRHS -- A `-1'-separated list of the rules' RHS.  */
static const yytype_int16 yyrhs[] =
{
     164,     0,    -1,   165,    -1,   166,    -1,   411,    -1,   235,
      -1,   174,    -1,   166,   174,    -1,   502,    -1,   504,    -1,
     505,    -1,   506,    -1,   503,    -1,   172,    -1,    52,    -1,
     506,    -1,   502,    -1,   171,   502,    -1,    -1,   505,    -1,
     505,   502,    -1,   502,    -1,   173,   149,   502,    -1,   175,
     176,    -1,    -1,   175,   177,    -1,   181,    -1,   196,    -1,
     202,    -1,   191,    -1,   208,    -1,   518,    -1,   523,    -1,
     178,    -1,   179,    -1,    44,   173,   152,    -1,    78,   180,
     152,    -1,   273,    -1,   180,   149,   273,    -1,   182,     1,
     470,   152,    -1,   182,   189,   187,   185,   184,   470,   152,
      -1,    31,   502,    42,    -1,    -1,   502,    -1,    -1,    17,
     361,    -1,    -1,   185,   186,    -1,   215,    -1,    -1,    -1,
      60,   188,   240,   152,    -1,    -1,    -1,    37,   190,   240,
     152,    -1,    37,     1,   152,    -1,   192,   194,    17,   361,
      30,   193,   152,    -1,   192,     1,    30,   193,   152,    -1,
      13,   502,    53,   502,    42,    -1,    -1,   502,    -1,    13,
     502,    -1,    13,    -1,    -1,   194,   195,    -1,   216,    -1,
     198,     1,    30,   199,   152,    -1,    -1,   198,   200,   475,
      30,   199,   152,   197,    -1,    24,   502,    53,   502,    42,
      -1,    -1,   502,    -1,    24,    -1,    24,   502,    -1,    -1,
     200,   201,    -1,   222,    -1,   203,     1,    30,   204,   152,
      -1,   203,   205,    30,   204,   152,    -1,    59,   502,    42,
      -1,    -1,   502,    -1,    59,   502,    -1,    59,    -1,    -1,
     207,    -1,   205,   206,    -1,   218,    -1,   560,   152,    -1,
     560,    -1,   560,   559,    -1,   560,   559,   152,    -1,   209,
       1,    30,   210,   152,    -1,   209,   211,    30,   210,   152,
      -1,    59,    19,   502,    42,    -1,    -1,   502,    -1,    59,
      19,    -1,    59,    19,   502,    -1,    -1,   211,   212,    -1,
     219,    -1,   202,    -1,   523,    -1,   208,    -1,   524,    -1,
     289,    -1,   313,    -1,   326,    -1,   338,    -1,   335,    -1,
     224,    -1,   179,    -1,   214,    -1,   235,    -1,   345,    -1,
     346,    -1,   341,    -1,   328,    -1,   331,    -1,   497,    -1,
     496,    -1,   213,    -1,   214,    -1,   235,    -1,   472,    -1,
     345,    -1,   346,    -1,   486,    -1,   341,    -1,   328,    -1,
     331,    -1,   497,    -1,   496,    -1,   213,    -1,   571,    -1,
     216,    -1,   217,   216,    -1,   214,    -1,   472,    -1,   345,
      -1,   346,    -1,   341,    -1,   328,    -1,   331,    -1,   497,
      -1,   496,    -1,   202,    -1,   523,    -1,   524,    -1,   214,
      -1,   235,    -1,   331,    -1,   497,    -1,   496,    -1,   345,
      -1,   346,    -1,   213,    -1,   214,    -1,   235,    -1,   345,
      -1,   346,    -1,   331,    -1,   497,    -1,   496,    -1,   213,
      -1,   220,    -1,   346,    -1,   179,    -1,   496,    -1,    78,
     118,   173,   152,    -1,    86,    -1,    85,    -1,   225,   152,
      -1,    -1,    61,   502,   226,   234,    -1,    -1,   223,    35,
     167,   227,   233,    67,   272,    -1,    -1,    35,   167,   228,
     233,    67,   272,    -1,   560,    -1,   560,   559,    -1,    -1,
     107,   231,   240,    -1,    -1,   232,   240,    -1,    -1,   229,
     230,    -1,   230,    -1,   229,    -1,   233,    -1,    -1,   225,
      42,   238,   236,    17,   424,    30,   237,   152,    -1,   225,
      42,     1,    30,   237,   152,    -1,    -1,   167,    -1,    35,
      -1,    61,    -1,    61,   502,    -1,    35,   502,    -1,    35,
       6,    -1,    -1,   238,   239,    -1,   220,    -1,   147,   243,
     241,   148,    -1,   147,     1,   148,    -1,    -1,   241,   242,
      -1,   152,   243,    -1,   558,    -1,   551,    -1,   247,   502,
      -1,   247,   173,   151,   246,   314,   245,   244,    -1,    -1,
     150,   260,    -1,    -1,    20,    -1,    21,    -1,    -1,   248,
      -1,    -1,   333,    -1,    40,    -1,    58,    -1,    41,    -1,
      20,    -1,    45,    -1,   147,   256,   250,   148,    -1,    -1,
     250,   251,    -1,   149,   256,    -1,   253,   253,    -1,   253,
      -1,   147,   257,   254,   148,    -1,   147,     1,   148,    -1,
     147,    55,   148,    -1,    -1,   254,   255,    -1,   149,   257,
      -1,   258,   153,   259,    -1,   259,    -1,   154,    -1,   102,
      -1,   260,    -1,   288,   153,   260,    -1,   323,    -1,   270,
      -1,   260,    -1,    55,    -1,   105,   260,    -1,   262,    -1,
     263,    -1,    91,    -1,    92,    -1,    93,    -1,    94,    -1,
      95,    -1,    96,    -1,   263,   261,   263,    -1,   263,    12,
     263,    -1,   263,    84,   263,    -1,   263,    56,   263,    -1,
     263,    51,   263,    -1,   263,    90,   263,    -1,   263,    48,
     263,    -1,   262,    48,   263,    -1,   262,    51,   263,    -1,
     262,    90,   263,    -1,   262,    12,   263,    -1,   262,    56,
     263,    -1,   262,    84,   263,    -1,   121,   269,    -1,   269,
      -1,   136,   269,    -1,   135,   269,    -1,   144,   269,    -1,
     143,   269,    -1,   269,   145,   269,    -1,   135,   269,   145,
     269,    -1,   263,   139,   263,    -1,   263,   138,   263,    -1,
     263,   134,   263,    -1,   263,   141,   263,    -1,   263,   136,
     263,    -1,   263,   135,   263,    -1,   263,   130,   263,    -1,
     263,   128,   263,    -1,   263,   131,   263,    -1,   263,   129,
     263,    -1,   263,   133,   263,    -1,   263,   132,   263,    -1,
     263,   140,   263,    -1,   263,   127,   263,    -1,   263,   126,
     263,    -1,   263,   125,   263,    -1,   263,   124,   263,    -1,
     263,   123,   263,    -1,   263,   122,   263,    -1,   135,   266,
      -1,   136,   266,    -1,   266,    -1,   264,   265,   266,    -1,
     134,    -1,   135,    -1,   136,    -1,   268,    -1,   268,   267,
     268,    -1,   141,    -1,   138,    -1,   139,    -1,   140,    -1,
     269,    -1,   144,   269,    -1,   143,   269,    -1,   269,   145,
     269,    -1,   270,    -1,   168,    -1,   278,    -1,   280,    -1,
     281,    -1,   147,   260,   148,    -1,   272,    -1,   271,    -1,
     562,    -1,   504,    -1,   277,    -1,   275,    -1,   502,    -1,
     273,    -1,   270,   156,   274,    -1,   167,    -1,   506,    -1,
      11,    -1,   272,   252,    -1,   271,   252,    -1,   146,    -1,
     272,   276,   502,    -1,   277,   147,   260,   148,    -1,   271,
     276,   502,    -1,   272,   276,    63,    -1,   271,   276,    63,
      -1,   279,   148,    -1,   147,   285,   153,   260,   148,    -1,
     147,   284,   149,   284,    -1,   279,   149,   284,    -1,   272,
     146,   147,   260,   148,    -1,   272,   146,   278,    -1,    49,
     272,   272,   283,    -1,    49,   272,   282,    -1,    49,   280,
      -1,    -1,   252,    -1,    -1,   252,    -1,   285,   153,   260,
      -1,   260,    -1,   288,   286,    -1,    -1,   286,   287,    -1,
     155,   288,    -1,   260,    -1,   323,    -1,    57,    -1,    75,
     502,     1,   152,    -1,    75,   502,   290,   152,    -1,    75,
       1,   152,    -1,    -1,    42,   291,    -1,   292,    -1,   318,
      -1,   295,    -1,   301,    -1,   305,    -1,   307,    -1,   311,
      -1,   312,    -1,   507,    -1,   512,    -1,   147,   169,   293,
     148,    -1,    -1,   293,   294,    -1,   149,   169,    -1,   318,
      76,   299,   297,    30,   296,    -1,    76,   502,    -1,    76,
      -1,    -1,   297,   298,    -1,   300,    -1,   502,   152,    -1,
     502,   133,   170,   152,    -1,    14,   147,   304,   302,   148,
      53,   314,    -1,    -1,   302,   303,    -1,   149,   304,    -1,
     272,    63,   154,    -1,    14,   319,    53,   314,    -1,    -1,
     502,    -1,    64,   310,   308,    30,    64,   306,    -1,    -1,
     308,   309,    -1,   310,    -1,   173,   151,   314,   152,    -1,
       8,   314,    -1,    33,    53,   272,    -1,    71,   502,    42,
     314,   152,    -1,    71,     1,   152,    -1,   272,   315,    -1,
     316,    -1,    -1,   252,    -1,   272,   272,   318,    -1,   272,
     318,    -1,   272,   272,   317,    -1,    -1,   252,    -1,    63,
     324,    -1,   147,   322,   320,   148,    -1,    -1,   320,   321,
      -1,   149,   322,    -1,   314,    -1,   324,    -1,   316,    -1,
     260,   325,   260,    -1,   277,    -1,   264,   325,   264,    -1,
      73,    -1,    27,    -1,    25,   173,   151,   314,   327,   152,
      -1,    -1,   150,   260,    -1,    70,   173,   151,   314,   330,
     329,   152,    -1,    -1,   150,   260,    -1,    -1,   334,    -1,
      79,   173,   151,   314,   332,   152,    -1,    89,    79,   173,
     151,   314,   332,   152,    -1,    -1,   150,   260,    -1,    25,
      -1,    70,    -1,    79,    -1,    89,    79,    -1,    33,    -1,
      75,    -1,    21,    -1,    65,    -1,    10,   336,   337,    42,
     270,   525,   152,    -1,    10,   336,   337,    42,     1,   152,
      -1,   502,    -1,   504,    -1,    -1,   151,   314,    -1,    33,
     173,   151,   314,    42,   340,   260,   152,    -1,    33,   173,
     151,   502,   339,   152,    -1,    -1,    55,   260,    42,   260,
      -1,    -1,   248,    -1,    26,   342,   151,   272,     9,   260,
     152,    -1,   270,   343,    -1,    57,    -1,    11,    -1,    -1,
     343,   344,    -1,   149,   270,    -1,    16,   502,   151,   272,
     152,    -1,    16,   502,    53,   347,    42,   260,   152,    -1,
     348,   525,   151,   351,    -1,   167,   349,    -1,    57,    -1,
      11,    -1,    -1,   349,   350,    -1,   149,   167,    -1,    31,
      -1,    13,    -1,    59,    -1,    24,    -1,    23,    -1,    43,
      -1,    75,    -1,    71,    -1,    61,    -1,    35,    -1,    70,
      -1,    79,    -1,    25,    -1,    87,    -1,    33,    -1,    76,
      -1,   106,    -1,   114,    -1,   109,    -1,   353,    -1,    39,
     260,    36,   404,   355,   354,    -1,    39,   464,   260,    36,
     404,   355,   354,    -1,    -1,    28,    36,   404,    -1,    28,
     464,    36,   404,    -1,    -1,   355,   356,    -1,    29,   260,
      36,   404,    -1,    29,   464,   260,    36,   404,    -1,   359,
      -1,   359,    -1,   360,    -1,    34,   502,    40,   322,    -1,
      34,   464,   502,    40,   322,    -1,    82,   260,    -1,   362,
      -1,    -1,   362,   363,    -1,   364,    -1,   365,    -1,   384,
      -1,   385,    -1,   386,    -1,   380,    -1,   405,    -1,   411,
      -1,    -1,   502,   151,    18,   366,   375,   374,   372,   370,
     368,    17,   361,    30,    18,   367,   152,    -1,    -1,   502,
      -1,    -1,   368,   369,    -1,   216,    -1,    -1,    60,   240,
     152,   371,    -1,    60,    47,   249,   152,    -1,    -1,    37,
     240,   152,   373,    -1,    -1,    37,    47,   249,   152,    -1,
      -1,   147,   260,   148,   375,    -1,    -1,    42,    -1,   502,
      -1,   376,   156,   502,    -1,   376,   378,    -1,   376,    -1,
     147,   502,   148,    -1,    24,    -1,    31,    -1,    23,    -1,
      -1,   502,   151,   270,   381,    37,    47,   249,   383,   152,
      -1,    -1,   502,   151,   270,   382,    60,    47,   249,   152,
      -1,   502,   151,   379,   377,    60,    47,   249,   152,    -1,
     502,   151,   379,   377,    37,    47,   249,   383,   152,    -1,
      -1,    60,    47,   249,    -1,   502,   151,   429,    -1,   429,
      -1,   502,   151,    88,   429,    -1,    88,   429,    -1,   502,
     151,   459,    -1,   459,    -1,   502,   151,    88,   459,    -1,
      88,   459,    -1,   502,   151,   387,    -1,   387,    -1,   502,
     151,    88,   387,    -1,    88,   387,    -1,   502,   151,    88,
     399,    -1,    88,   399,    -1,   502,   151,   399,    -1,   399,
      -1,   395,   130,   396,   388,   152,    -1,   389,    -1,   389,
      81,   260,    -1,   389,    81,   260,    28,   388,    -1,   392,
     390,    -1,    97,    -1,    -1,   390,   391,    -1,   149,   392,
      -1,   260,   393,    -1,    -1,     9,   260,    -1,    52,   394,
      -1,    52,    -1,     9,   260,    -1,   270,    -1,   278,    -1,
     398,   397,    -1,    -1,    74,    -1,   110,   260,   105,    -1,
     105,    -1,    -1,    38,    -1,    83,   260,    68,   395,   130,
     396,   400,   152,    -1,   401,   389,    81,   285,    -1,    -1,
     401,   402,    -1,   389,    81,   285,   149,    -1,    -1,   217,
      17,    -1,    17,    -1,   403,   361,    -1,    -1,   502,   151,
     406,   357,    36,   403,   361,   407,    -1,   410,    30,   409,
     152,    -1,    30,   409,   152,    -1,    -1,   502,   151,   408,
     352,   407,    -1,   502,   151,   529,    -1,    36,    -1,    36,
     502,    -1,    30,   152,    -1,    30,   502,   152,    -1,   502,
     151,   412,    -1,   412,    -1,    -1,    -1,   416,   413,    62,
     420,   418,   414,    17,   424,    30,   415,   152,    -1,     1,
      30,   415,   152,    -1,    -1,   416,    62,   417,    -1,    -1,
      88,    -1,    -1,   502,    -1,    -1,    42,    -1,   418,   419,
      -1,   221,    -1,    -1,   147,    11,   148,    -1,   147,   421,
     148,    -1,   270,   422,    -1,    -1,   422,   423,    -1,   149,
     270,    -1,   425,    -1,    -1,   425,   426,    -1,   427,    -1,
     429,    -1,   464,   429,    -1,   434,    -1,   452,    -1,   441,
      -1,   448,    -1,   455,    -1,   458,    -1,   459,    -1,   460,
      -1,   464,   462,    -1,   462,    -1,   463,    -1,   466,    -1,
     464,   466,    -1,   428,    -1,   451,    66,   260,   430,   152,
      -1,    15,   260,   431,   430,   152,    -1,    -1,    69,   260,
      -1,    -1,    66,   260,    -1,    -1,   157,    -1,    -1,   157,
      -1,   502,    -1,    -1,    22,   432,   260,   435,    42,   439,
     437,    30,    22,   433,   152,    -1,    -1,   464,    22,   432,
     260,   436,    42,   439,   437,    30,    22,   433,   152,    -1,
      22,     1,    30,    22,   433,   152,    -1,    -1,   437,   438,
      -1,   439,    -1,    -1,    81,   285,   153,   440,   424,    -1,
      -1,    39,   260,    72,   442,   424,   445,   443,    30,    39,
     152,    -1,    -1,    -1,    28,   444,   424,    -1,    -1,   445,
     446,    -1,    -1,    29,   260,    72,   447,   424,    -1,   451,
     450,    46,   424,    30,    46,   449,   152,    -1,    -1,   502,
      -1,    -1,   358,    -1,    -1,   502,   151,    -1,   451,    32,
     454,   453,   152,    -1,    -1,    81,   260,    -1,    -1,   502,
      -1,   451,    50,   457,   456,   152,    -1,    -1,    81,   260,
      -1,    -1,   502,    -1,    52,   152,    -1,   270,   152,    -1,
      67,   461,   152,    -1,    -1,   260,    -1,   395,   130,   389,
     152,    -1,   395,   130,   544,   389,   152,    -1,   395,   130,
     104,   543,   260,   152,    -1,   395,   130,   111,   543,   152,
      -1,   537,    -1,   533,    -1,   465,   152,    -1,   545,    -1,
     464,   547,    -1,   547,    -1,   502,   151,    -1,   395,   150,
     260,    -1,   464,   395,   150,   260,    -1,    80,   469,   468,
     467,   152,    -1,    -1,    34,   260,    -1,    -1,    77,   260,
      -1,    -1,    54,   421,    -1,    30,    -1,    30,    23,   183,
      -1,    30,    13,   183,    -1,    30,    31,   183,    -1,    30,
     502,    -1,    -1,    42,    -1,    23,   502,   471,   474,   473,
     470,   152,    -1,    -1,    60,   240,   152,    -1,    -1,    37,
     240,   152,    -1,    -1,    34,   481,   479,   477,   476,    30,
      34,   152,    -1,    34,     1,    30,    34,   152,    -1,    -1,
     477,   478,    -1,   482,    -1,    -1,   479,   480,    -1,   179,
      -1,   270,    -1,   475,    -1,   483,    -1,    34,   488,   485,
     484,    30,    34,   152,    -1,    -1,   475,    -1,    -1,   492,
     491,   152,    -1,    78,   118,   173,   152,    -1,    78,   490,
     152,    -1,    34,   488,   487,   152,    -1,    34,   488,   487,
     152,    30,    34,   152,    -1,    78,   490,    -1,    78,   118,
     173,   152,    -1,   492,   491,    -1,   489,   151,   260,    -1,
     173,    -1,    11,    -1,    57,    -1,   493,   492,   491,    -1,
      -1,    60,    47,   249,    -1,    -1,    37,    47,   249,    -1,
      31,   270,    -1,    24,   272,    -1,    55,    -1,   502,    -1,
     506,    -1,   494,    -1,   495,   149,   494,    -1,    87,   502,
     151,   501,   147,   495,   148,   152,    -1,    87,   502,    42,
     147,   500,   148,   152,    -1,    87,   502,    42,   147,     1,
     152,   148,    -1,   351,   499,    -1,    -1,   154,    -1,   498,
      -1,   500,   149,   498,    -1,   502,    -1,   504,    -1,     7,
      -1,     5,    -1,     6,    -1,     3,    -1,     4,    -1,   108,
     508,    30,   510,    -1,   108,     1,    30,   510,    -1,    -1,
     508,   509,    -1,   511,    -1,   108,    -1,   108,   502,    -1,
     179,    -1,   346,    -1,   224,    -1,   524,    -1,   108,    19,
     513,    30,   515,    -1,   108,    19,     1,    30,   515,    -1,
      -1,   513,   514,    -1,   516,    -1,   108,    19,    -1,   108,
      19,   502,    -1,   220,    -1,   100,   180,   152,    -1,    -1,
     100,   502,    42,   519,   521,    30,   520,   152,    -1,   100,
     502,    42,    30,   520,   152,    -1,   100,    -1,   100,   502,
      -1,   522,    -1,   521,   522,    -1,   179,    -1,   178,    -1,
     517,    -1,    59,   502,    42,    49,   376,   525,   152,    -1,
      59,   502,    42,    49,   376,   525,   559,   152,    -1,    59,
       1,   502,    42,    49,   152,    -1,    35,   502,    42,    49,
     376,   525,   152,    -1,    35,   502,    42,    49,   376,   525,
     559,   152,    -1,    35,   502,    42,    49,     1,   152,    -1,
      -1,   160,   526,   161,    -1,   160,   161,    -1,    67,   272,
      -1,   527,    -1,   527,    67,   272,    -1,   272,    -1,   527,
     528,    -1,   149,   272,    -1,    22,   260,    36,   531,   532,
      30,    36,   409,   152,    -1,    22,   260,    36,   531,    30,
      36,   409,   152,    -1,    22,     1,    36,     1,    30,    36,
     409,   152,    -1,    81,   464,   285,   153,   404,    -1,    81,
     285,   153,   404,    -1,   531,   530,    -1,   530,    -1,    30,
     152,    -1,    30,   502,   152,    -1,   534,    -1,   536,    -1,
     395,   130,   392,    81,   260,   535,   152,    -1,   395,   130,
     544,   392,    81,   260,   535,   152,    -1,   395,   130,   392,
      81,   260,   152,    -1,   395,   130,   544,   392,    81,   260,
     152,    -1,   395,   130,     1,   152,    -1,    28,   260,    81,
     260,    -1,    28,   260,    -1,   395,   130,   104,   543,   260,
      81,   260,   546,   152,    -1,   395,   130,   104,   543,   260,
      81,   260,   152,    -1,   538,    -1,   542,    -1,    83,   260,
      68,   432,   395,   130,   539,   540,    -1,    -1,   544,    -1,
     392,    81,   285,   149,   540,    -1,   541,    -1,   392,    81,
     285,   152,    -1,    83,   260,    68,   432,   395,   130,   104,
     543,   548,    -1,    -1,    40,    -1,    58,    -1,    74,    -1,
     110,   260,   105,    -1,   105,    -1,   465,    81,   260,   546,
     152,    -1,   465,    81,   260,   152,    -1,    28,   260,    81,
     260,    -1,   546,    28,   260,    81,   260,    -1,    28,   260,
      -1,    83,   260,    68,   432,   550,   150,   548,    -1,   260,
      81,   285,   149,   548,    -1,   549,    -1,   260,    81,   285,
     152,    -1,   270,    -1,   278,    -1,   552,    -1,   553,    -1,
      61,   502,   556,    -1,    35,   554,   556,    67,   272,   555,
      -1,   223,    35,   554,   556,    67,   272,   555,    -1,   502,
      -1,   504,    -1,    -1,    42,   502,    -1,    42,   154,    -1,
      -1,   107,    -1,    -1,   107,   557,   147,   243,   241,   148,
      -1,   147,   243,   241,   148,    -1,    59,   502,    42,    49,
     376,    -1,    59,   502,    42,    49,   376,   559,    -1,    37,
      47,   249,    -1,    -1,    37,   561,   240,    -1,   119,   563,
     564,   151,   314,   120,    -1,    25,    -1,    70,    -1,    79,
      -1,   565,    -1,   566,    -1,   570,    -1,   156,   569,   502,
      -1,   156,   502,    -1,   567,   569,   502,    -1,   567,   502,
      -1,   159,   156,    -1,   567,   159,   156,    -1,   502,    -1,
     502,   147,   260,   148,    -1,   568,   156,    -1,   569,   568,
     156,    -1,   158,   376,    -1,   162,    -1
};

/* YYRLINE[YYN] -- source line where rule number YYN was defined.  */
static const yytype_uint16 yyrline[] =
{
       0,   400,   400,   403,   405,   406,   408,   409,   412,   413,
     416,   417,   418,   419,   420,   423,   424,   426,   428,   429,
     431,   433,   434,   441,   443,   444,   446,   447,   448,   449,
     450,   451,   452,   455,   456,   459,   468,   483,   484,   490,
     492,   495,   505,   506,   508,   509,   511,   512,   515,   517,
     518,   518,   520,   521,   521,   522,   526,   527,   529,   537,
     538,   539,   540,   542,   543,   545,   547,   548,   548,   556,
     563,   564,   569,   570,   571,   572,   573,   575,   576,   577,
     591,   592,   593,   594,   596,   597,   598,   599,   601,   602,
     603,   604,   606,   607,   608,   615,   616,   617,   618,   620,
     621,   622,   629,   630,   631,   632,   635,   636,   637,   638,
     639,   640,   641,   643,   644,   645,   646,   647,   648,   649,
     650,   651,   652,   655,   656,   657,   658,   659,   660,   661,
     662,   663,   664,   665,   666,   667,   669,   670,   673,   674,
     675,   676,   677,   678,   679,   680,   681,   682,   683,   684,
     686,   687,   688,   689,   690,   691,   692,   693,   695,   696,
     697,   698,   699,   700,   701,   702,   704,   706,   707,   708,
     709,   714,   715,   717,   720,   719,   727,   726,   739,   738,
     751,   752,   755,   754,   758,   758,   762,   763,   764,   765,
     767,   770,   769,   783,   787,   788,   789,   790,   791,   792,
     793,   795,   797,   799,   805,   806,   807,   808,   809,   811,
     819,   831,   838,   858,   859,   860,   861,   862,   863,   864,
     865,   866,   868,   869,   870,   871,   872,   874,   875,   876,
     877,   879,   881,   883,   890,   891,   893,   894,   895,   897,
     898,   899,   900,   903,   904,   905,   907,   909,   910,   911,
     917,   918,   920,   921,   922,   923,   924,   925,   927,   928,
     929,   930,   931,   932,   933,   934,   935,   936,   937,   938,
     939,   943,   944,   945,   946,   947,   948,   949,   950,   954,
     955,   956,   957,   958,   959,   960,   961,   962,   963,   964,
     965,   966,   967,   968,   969,   970,   971,   972,   974,   975,
     976,   977,   980,   981,   982,   985,   986,   989,   990,   991,
     992,   994,   995,   996,   997,   999,  1000,  1001,  1002,  1003,
    1004,  1006,  1007,  1008,  1009,  1010,  1011,  1013,  1014,  1016,
    1018,  1019,  1020,  1022,  1023,  1025,  1028,  1029,  1030,  1031,
    1032,  1034,  1035,  1037,  1038,  1040,  1041,  1043,  1044,  1045,
    1046,  1047,  1048,  1049,  1056,  1057,  1059,  1060,  1061,  1062,
    1064,  1065,  1066,  1071,  1072,  1078,  1080,  1081,  1083,  1084,
    1085,  1086,  1087,  1088,  1089,  1090,  1091,  1092,  1095,  1096,
    1097,  1098,  1100,  1109,  1110,  1112,  1113,  1114,  1116,  1118,
    1120,  1129,  1130,  1131,  1133,  1135,  1137,  1138,  1140,  1152,
    1153,  1157,  1159,  1161,  1163,  1169,  1173,  1174,  1175,  1176,
    1177,  1179,  1180,  1181,  1182,  1183,  1185,  1188,  1191,  1192,
    1194,  1196,  1197,  1199,  1200,  1202,  1203,  1205,  1206,  1212,
    1221,  1222,  1224,  1229,  1230,  1231,  1232,  1234,  1239,  1244,
    1245,  1247,  1248,  1249,  1250,  1251,  1252,  1254,  1255,  1257,
    1265,  1267,  1268,  1270,  1271,  1274,  1279,  1285,  1286,  1289,
    1290,  1292,  1295,  1296,  1297,  1298,  1299,  1300,  1306,  1312,
    1319,  1321,  1322,  1323,  1324,  1325,  1326,  1328,  1329,  1330,
    1331,  1332,  1333,  1334,  1335,  1336,  1337,  1338,  1339,  1340,
    1341,  1342,  1343,  1344,  1345,  1346,  1353,  1355,  1356,  1358,
    1359,  1360,  1361,  1362,  1363,  1364,  1366,  1368,  1369,  1371,
    1384,  1391,  1401,  1402,  1403,  1404,  1406,  1407,  1408,  1409,
    1410,  1414,  1415,  1417,  1417,  1424,  1425,  1426,  1427,  1428,
    1429,  1430,  1432,  1433,  1434,  1435,  1436,  1437,  1438,  1439,
    1440,  1442,  1443,  1446,  1447,  1449,  1451,  1452,  1453,  1455,
    1455,  1459,  1459,  1464,  1468,  1472,  1473,  1475,  1476,  1478,
    1479,  1481,  1482,  1484,  1485,  1487,  1488,  1490,  1491,  1493,
    1494,  1496,  1497,  1499,  1501,  1502,  1503,  1505,  1506,  1507,
    1508,  1509,  1511,  1512,  1513,  1514,  1515,  1516,  1518,  1519,
    1521,  1523,  1524,  1525,  1526,  1528,  1529,  1531,  1534,  1535,
    1536,  1537,  1539,  1540,  1541,  1547,  1550,  1549,  1555,  1556,
    1559,  1558,  1561,  1563,  1564,  1567,  1568,  1570,  1579,  1588,
    1594,  1587,  1610,  1612,  1613,  1615,  1616,  1618,  1619,  1621,
    1622,  1623,  1624,  1625,  1626,  1627,  1629,  1630,  1631,  1632,
    1638,  1639,  1640,  1641,  1642,  1643,  1644,  1645,  1646,  1647,
    1648,  1649,  1650,  1651,  1652,  1653,  1654,  1655,  1656,  1657,
    1659,  1661,  1662,  1663,  1664,  1665,  1667,  1668,  1670,  1671,
    1672,  1676,  1675,  1688,  1687,  1699,  1700,  1701,  1702,  1704,
    1703,  1712,  1711,  1724,  1726,  1725,  1734,  1735,  1737,  1736,
    1742,  1750,  1751,  1752,  1755,  1756,  1757,  1759,  1764,  1765,
    1766,  1767,  1770,  1776,  1777,  1778,  1779,  1781,  1783,  1788,
    1789,  1790,  1792,  1793,  1794,  1795,  1796,  1797,  1800,  1801,
    1802,  1803,  1805,  1806,  1807,  1809,  1814,  1815,  1816,  1817,
    1818,  1819,  1825,  1826,  1827,  1828,  1829,  1831,  1832,  1834,
    1839,  1840,  1841,  1842,  1844,  1844,  1848,  1849,  1850,  1851,
    1852,  1853,  1854,  1856,  1866,  1867,  1869,  1873,  1874,  1875,
    1877,  1881,  1882,  1887,  1891,  1896,  1899,  1903,  1908,  1914,
    1915,  1916,  1918,  1920,  1921,  1923,  1924,  1927,  1928,  1929,
    1932,  1933,  1936,  1937,  1940,  1947,  1953,  1956,  1958,  1959,
    1961,  1962,  1966,  1967,  1969,  1974,  1979,  1984,  1989,  1999,
    2000,  2002,  2003,  2004,  2005,  2006,  2008,  2009,  2010,  2011,
    2013,  2014,  2016,  2017,  2018,  2020,  2021,  2023,  2029,  2031,
    2031,  2037,  2042,  2043,  2045,  2046,  2048,  2049,  2050,  2052,
    2060,  2065,  2067,  2072,  2077,  2079,  2080,  2082,  2084,  2085,
    2086,  2088,  2089,  2090,  2092,  2093,  2094,  2096,  2097,  2098,
    2099,  2101,  2102,  2104,  2105,  2107,  2108,  2109,  2110,  2111,
    2113,  2114,  2116,  2117,  2119,  2120,  2122,  2125,  2126,  2128,
    2129,  2131,  2133,  2136,  2137,  2138,  2140,  2141,  2142,  2144,
    2145,  2147,  2148,  2149,  2151,  2153,  2154,  2156,  2158,  2159,
    2161,  2162,  2164,  2166,  2176,  2187,  2188,  2191,  2192,  2193,
    2195,  2196,  2197,  2197,  2201,  2203,  2208,  2214,  2217,  2216,
    2227,  2234,  2235,  2236,  2238,  2239,  2240,  2243,  2244,  2246,
    2247,  2249,  2250,  2252,  2253,  2256,  2257,  2259,  2261
};
#endif

#if YYDEBUG || YYERROR_VERBOSE || YYTOKEN_TABLE
/* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
   First, the terminals, then, starting at YYNTOKENS, nonterminals.  */
static const char *const yytname[] =
{
  "$end", "error", "$undefined", "t_ABSTRLIST", "t_CHARLIST", "t_DIGIT",
  "t_STRING", "t_LETTER", "t_ACCESS", "t_AFTER", "t_ALIAS", "t_ALL",
  "t_AND", "t_ARCHITECTURE", "t_ARRAY", "t_ASSERT", "t_ATTRIBUTE",
  "t_BEGIN", "t_BLOCK", "t_BODY", "t_BUFFER", "t_BUS", "t_CASE",
  "t_COMPONENT", "t_CONFIGURATION", "t_CONSTANT", "t_DISCONNECT",
  "t_DOWNTO", "t_ELSE", "t_ELSIF", "t_END", "t_ENTITY", "t_EXIT", "t_FILE",
  "t_FOR", "t_FUNCTION", "t_GENERATE", "t_GENERIC", "t_GUARDED", "t_IF",
  "t_IN", "t_INOUT", "t_IS", "t_LABEL", "t_LIBRARY", "t_LINKAGE", "t_LOOP",
  "t_MAP", "t_NAND", "t_NEW", "t_NEXT", "t_NOR", "t_NULL", "t_OF", "t_ON",
  "t_OPEN", "t_OR", "t_OTHERS", "t_OUT", "t_PACKAGE", "t_PORT",
  "t_PROCEDURE", "t_PROCESS", "t_RANGE", "t_RECORD", "t_REGISTER",
  "t_REPORT", "t_RETURN", "t_SELECT", "t_SEVERITY", "t_SIGNAL",
  "t_SUBTYPE", "t_THEN", "t_TO", "t_TRANSPORT", "t_TYPE", "t_UNITS",
  "t_UNTIL", "t_USE", "t_VARIABLE", "t_WAIT", "t_WHEN", "t_WHILE",
  "t_WITH", "t_XOR", "t_IMPURE", "t_PURE", "t_GROUP", "t_POSTPONED",
  "t_SHARED", "t_XNOR", "t_SLL", "t_SRA", "t_SLA", "t_SRL", "t_ROR",
  "t_ROL", "t_UNAFFECTED", "t_ASSUME_GUARANTEE", "t_ASSUME", "t_CONTEXT",
  "t_COVER", "t_DEFAULT", "t_FAIRNESS", "t_FORCE", "t_INERTIAL",
  "t_LITERAL", "t_PARAMETER", "t_PROTECTED", "t_PROPERTY", "t_REJECT",
  "t_RELEASE", "t_RESTRICT", "t_RESTRICT_GUARANTEE", "t_SEQUENCE",
  "t_STRONG", "t_VMODE", "t_VPROP", "t_VUNIT", "t_SLSL", "t_SRSR", "t_QQ",
  "t_QGT", "t_QLT", "t_QG", "t_QL", "t_QEQU", "t_QNEQU", "t_GESym",
  "t_GTSym", "t_LESym", "t_LTSym", "t_NESym", "t_EQSym", "t_Ampersand",
  "t_Minus", "t_Plus", "MED_PRECEDENCE", "t_REM", "t_MOD", "t_Slash",
  "t_Star", "MAX_PRECEDENCE", "t_NOT", "t_ABS", "t_DoubleStar",
  "t_Apostrophe", "t_LeftParen", "t_RightParen", "t_Comma", "t_VarAsgn",
  "t_Colon", "t_Semicolon", "t_Arrow", "t_Box", "t_Bar", "t_Dot", "t_Q",
  "t_At", "t_Neg", "t_LEFTBR", "t_RIGHTBR", "t_ToolDir", "$accept",
  "start", "design_file", "design_unit_list", "designator", "literal",
  "enumeration_literal", "physical_literal", "physical_literal_1",
  "physical_literal_no_default", "idf_list", "design_unit", "context_list",
  "lib_unit", "context_item", "lib_clause", "use_clause", "sel_list",
  "entity_decl", "entity_start", "entity_decl_5", "entity_decl_4",
  "entity_decl_3", "entity_decl_6", "entity_decl_2", "@1", "entity_decl_1",
  "@2", "arch_body", "arch_start", "arch_body_2", "arch_body_1",
  "arch_body_3", "config_decl", "@3", "config_start", "config_decl_2",
  "config_decl_1", "config_decl_3", "package_decl", "package_start",
  "package_decl_2", "package_decl_1", "package_decl_3", "package_decl_22",
  "package_body", "pack_body_start", "package_body_2", "package_body_1",
  "package_body_3", "common_decltve_item_1", "common_decltve_item",
  "entity_decltve_item", "block_decltve_item", "block_declarative_part",
  "package_decltve_item", "package_body_decltve_item",
  "subprog_decltve_item", "procs_decltve_item", "config_decltve_item",
  "func_prec", "subprog_decl", "subprog_spec", "@4", "@5", "@6",
  "subprog_spec_22", "subprog_spec_33", "@7", "@8", "subprog_spec_2",
  "subprog_spec_1", "subprog_body", "@9", "subprog_body_2",
  "subprog_body_1", "subprog_body_3", "interf_list", "interf_list_1",
  "interf_list_2", "interf_element", "interf_element_4",
  "interf_element_3", "interf_element_2", "interf_element_1", "mode",
  "association_list", "association_list_1", "association_list_2",
  "gen_association_list", "gen_assoc", "gen_association_list_1",
  "gen_association_list_2", "association_element",
  "gen_association_element", "formal_part", "actual_part", "expr",
  "shift_op", "and_relation", "relation", "simple_exp", "adding_op",
  "term", "multiplying_operator", "factor", "primary", "name", "name2",
  "mark", "sel_name", "suffix", "ifts_name", "sigma", "attribute_name",
  "aggregate", "element_association_list2", "qualified_expr", "allocator",
  "allocator_2", "allocator_1", "element_association", "choices",
  "choices_1", "choices_2", "choice", "type_decl", "type_decl_1",
  "type_definition", "enumeration_type_definition",
  "enumeration_type_definition_1", "enumeration_type_definition_2",
  "physical_type_definition", "unit_stat", "physical_type_definition_1",
  "physical_type_definition_2", "base_unit_decl", "secondary_unit_decl",
  "unconstrained_array_definition", "unconstrained_array_definition_1",
  "unconstrained_array_definition_2", "index_subtype_definition",
  "constrained_array_definition", "record_type_simple_name",
  "record_type_definition", "record_type_definition_1",
  "record_type_definition_2", "element_decl", "access_type_definition",
  "file_type_definition", "subtype_decl", "subtype_indic",
  "subtype_indic_1", "subtype_indic1", "subtype_indic1_1",
  "range_constraint", "index_constraint", "index_constraint_1",
  "index_constraint_2", "discrete_range", "discrete_range1", "range_spec",
  "direction", "constant_decl", "constant_decl_1", "signal_decl",
  "signal_decl_2", "signal_decl_1", "variable_decl", "variable_decl_1",
  "object_class", "signal_kind", "alias_decl", "alias_name_stat",
  "alias_spec", "file_decl", "fi_dec", "file_decl_1", "disconnection_spec",
  "signal_list", "signal_list_1", "signal_list_2", "attribute_decl",
  "attribute_spec", "entity_spec", "entity_name_list",
  "entity_name_list_1", "entity_name_list_2", "entity_class",
  "if_generation_scheme", "if_scheme", "if_scheme_2", "if_scheme_1",
  "if_scheme_3", "generation_scheme", "iteration_scheme", "for_scheme",
  "while_scheme", "concurrent_stats", "concurrent_stats_1",
  "concurrent_stats_2", "concurrent_stat", "block_stat", "@10",
  "block_stat_5", "block_stat_4", "block_stat_6", "block_stat_3",
  "block_stat_7", "block_stat_2", "block_stat_8", "block_stat_1",
  "block_stat_0", "dot_name", "mark_comp", "comp_1", "vcomp_stat",
  "comp_inst_stat", "@11", "@12", "comp_inst_stat_1",
  "concurrent_assertion_stat", "concurrent_procedure_call",
  "concurrent_signal_assign_stat", "condal_signal_assign",
  "condal_wavefrms", "wavefrm", "wavefrm_1", "wavefrm_2",
  "wavefrm_element", "wavefrm_element_1", "wavefrm_element_2", "target",
  "opts", "opts_2", "opts_1", "sel_signal_assign", "sel_wavefrms",
  "sel_wavefrms_1", "sel_wavefrms_2", "gen_stat1",
  "generate_statement_body", "generate_stat", "@13", "opstat", "@14",
  "generate_stat_1", "end_stats", "procs_stat", "procs_stat1", "@15",
  "@16", "procs_stat1_3", "procs_stat1_5", "procs_stat1_6",
  "procs_stat1_2", "procs_stat1_4", "procs_stat1_1", "sensitivity_list",
  "sensitivity_list_1", "sensitivity_list_2", "seq_stats", "seq_stats_1",
  "seq_stats_2", "seq_stat", "report_statement", "assertion_stat",
  "assertion_stat_2", "assertion_stat_1", "choice_stat", "choice_stat_1",
  "case_stat", "@17", "@18", "case_stat_1", "case_stat_2",
  "case_stat_alternative", "@19", "if_stat", "@20", "if_stat_2", "@21",
  "if_stat_1", "if_stat_3", "@22", "loop_stat", "loop_stat_3",
  "loop_stat_2", "loop_stat_1", "exit_stat", "exit_stat_2", "exit_stat_1",
  "next_stat", "next_stat_2", "next_stat_1", "null_stat",
  "procedure_call_stat", "return_stat", "return_stat_1",
  "signal_assign_stat", "variable_assign_stat", "lable",
  "variable_assign_stat_1", "wait_stat", "wait_stat_3", "wait_stat_2",
  "wait_stat_1", "comp_end_dec", "iss", "comp_decl", "comp_decl_2",
  "comp_decl_1", "block_config", "@23", "block_config_2", "block_config_3",
  "block_config_1", "block_config_4", "block_spec", "config_item",
  "comp_config", "comp_config_2", "comp_config_1", "config_spec",
  "comp_spec_stat", "comp_spec", "inst_list", "binding_indic",
  "binding_indic_2", "binding_indic_1", "entity_aspect",
  "group_constituent", "group_constituent_list", "group_declaration",
  "group_template_declaration", "entity_class_entry", "tbox",
  "entity_class_entry_list", "group_name", "t_Identifier",
  "t_BitStringLit", "t_StringLit", "t_AbstractLit", "t_CharacterLit",
  "protected_type_declaration", "protected_stats", "protected_stat_decl_1",
  "protected_stat_1", "protected_type_declaration_item",
  "protected_type_body", "protected_body_stats",
  "protected_body_stat_decl_1", "protected_body_stat_1",
  "protected_type_body_declaration_item", "context_ref", "context_decl",
  "@24", "context_stat_1", "libustcont_stats", "libustcont_stat",
  "package_instantiation_decl", "subprogram_instantiation_decl",
  "signature", "signature1", "mark_stats", "mark_stats_1", "case_scheme",
  "when_stats_1", "when_stats", "ttend", "conditional_signal_assignment",
  "conditional_waveform_assignment", "else_wave_list",
  "conditional_force_assignment", "selected_signal_assignment",
  "selected_waveform_assignment", "delay_stat", "sel_wave_list",
  "sel_wave_list_1", "selected_force_assignment", "inout_stat",
  "delay_mechanism", "conditional_variable_assignment", "else_stat",
  "selected_variable_assignment", "sel_var_list", "sel_var_list_1",
  "select_name", "interface_subprogram_decl", "iproc", "ifunc",
  "func_name", "return_is", "param", "@25", "interface_package_decl",
  "gen_assoc_list", "gen_interface_list", "@26", "external_name",
  "sig_stat", "external_pathname", "absolute_pathname",
  "relative_pathname", "neg_list", "pathname_element",
  "pathname_element_list", "package_path_name", "tool_directive", 0
};
#endif

# ifdef YYPRINT
/* YYTOKNUM[YYLEX-NUM] -- Internal token number corresponding to
   token YYLEX-NUM.  */
static const yytype_uint16 yytoknum[] =
{
       0,   256,   257,   258,   259,   260,   261,   262,   263,   264,
     265,   266,   267,   268,   269,   270,   271,   272,   273,   274,
     275,   276,   277,   278,   279,   280,   281,   282,   283,   284,
     285,   286,   287,   288,   289,   290,   291,   292,   293,   294,
     295,   296,   297,   298,   299,   300,   301,   302,   303,   304,
     305,   306,   307,   308,   309,   310,   311,   312,   313,   314,
     315,   316,   317,   318,   319,   320,   321,   322,   323,   324,
     325,   326,   327,   328,   329,   330,   331,   332,   333,   334,
     335,   336,   337,   338,   339,   340,   341,   342,   343,   344,
     345,   346,   347,   348,   349,   350,   351,   352,   353,   354,
     355,   356,   357,   358,   359,   360,   361,   362,   363,   364,
     365,   366,   367,   368,   369,   370,   371,   372,   373,   374,
     375,   376,   377,   378,   379,   380,   381,   382,   383,   384,
     385,   386,   387,   388,   389,   390,   391,   392,   393,   394,
     395,   396,   397,   398,   399,   400,   401,   402,   403,   404,
     405,   406,   407,   408,   409,   410,   411,   412,   413,   414,
     415,   416,   417
};
# endif

/* YYR1[YYN] -- Symbol number of symbol that rule YYN derives.  */
static const yytype_uint16 yyr1[] =
{
       0,   163,   164,   165,   165,   165,   166,   166,   167,   167,
     168,   168,   168,   168,   168,   169,   169,   170,   171,   171,
     172,   173,   173,   174,   175,   175,   176,   176,   176,   176,
     176,   176,   176,   177,   177,   178,   179,   180,   180,   181,
     181,   182,   183,   183,   184,   184,   185,   185,   186,   187,
     188,   187,   189,   190,   189,   189,   191,   191,   192,   193,
     193,   193,   193,   194,   194,   195,   196,   197,   196,   198,
     199,   199,   199,   199,   200,   200,   201,   202,   202,   203,
     204,   204,   204,   204,   205,   205,   205,   206,   207,   207,
     207,   207,   208,   208,   209,   210,   210,   210,   210,   211,
     211,   212,   213,   213,   213,   213,   214,   214,   214,   214,
     214,   214,   214,   215,   215,   215,   215,   215,   215,   215,
     215,   215,   215,   216,   216,   216,   216,   216,   216,   216,
     216,   216,   216,   216,   216,   216,   217,   217,   218,   218,
     218,   218,   218,   218,   218,   218,   218,   218,   218,   218,
     219,   219,   219,   219,   219,   219,   219,   219,   220,   220,
     220,   220,   220,   220,   220,   220,   221,   222,   222,   222,
     222,   223,   223,   224,   226,   225,   227,   225,   228,   225,
     229,   229,   231,   230,   232,   230,   233,   233,   233,   233,
     234,   236,   235,   235,   237,   237,   237,   237,   237,   237,
     237,   238,   238,   239,   240,   240,   241,   241,   242,   243,
     243,   243,   243,   244,   244,   245,   245,   245,   246,   246,
     247,   247,   248,   248,   248,   248,   248,   249,   250,   250,
     251,   252,   252,   253,   252,   252,   254,   254,   255,   256,
     256,   256,   256,   257,   257,   257,   258,   259,   259,   259,
     260,   260,   261,   261,   261,   261,   261,   261,   262,   262,
     262,   262,   262,   262,   262,   262,   262,   262,   262,   262,
     262,   263,   263,   263,   263,   263,   263,   263,   263,   263,
     263,   263,   263,   263,   263,   263,   263,   263,   263,   263,
     263,   263,   263,   263,   263,   263,   263,   263,   264,   264,
     264,   264,   265,   265,   265,   266,   266,   267,   267,   267,
     267,   268,   268,   268,   268,   269,   269,   269,   269,   269,
     269,   270,   270,   270,   271,   271,   271,   272,   272,   273,
     274,   274,   274,   275,   275,   276,   277,   277,   277,   277,
     277,   278,   278,   279,   279,   280,   280,   281,   281,   281,
     282,   282,   283,   283,   284,   284,   285,   286,   286,   287,
     288,   288,   288,   289,   289,   289,   290,   290,   291,   291,
     291,   291,   291,   291,   291,   291,   291,   291,   292,   293,
     293,   294,   295,   296,   296,   297,   297,   298,   299,   300,
     301,   302,   302,   303,   304,   305,   306,   306,   307,   308,
     308,   309,   310,   311,   312,   313,   313,   314,   314,   315,
     315,   316,   316,   316,   317,   317,   318,   319,   320,   320,
     321,   322,   322,   323,   323,   324,   324,   325,   325,   326,
     327,   327,   328,   329,   329,   330,   330,   331,   331,   332,
     332,   333,   333,   333,   333,   333,   333,   334,   334,   335,
     335,   336,   336,   337,   337,   338,   338,   339,   339,   340,
     340,   341,   342,   342,   342,   343,   343,   344,   345,   346,
     347,   348,   348,   348,   349,   349,   350,   351,   351,   351,
     351,   351,   351,   351,   351,   351,   351,   351,   351,   351,
     351,   351,   351,   351,   351,   351,   352,   353,   353,   354,
     354,   354,   355,   355,   356,   356,   357,   358,   358,   359,
     359,   360,   361,   362,   362,   363,   364,   364,   364,   364,
     364,   364,   364,   366,   365,   367,   367,   368,   368,   369,
     370,   370,   371,   372,   372,   373,   373,   374,   374,   375,
     375,   376,   376,   377,   377,   378,   379,   379,   379,   381,
     380,   382,   380,   380,   380,   383,   383,   384,   384,   384,
     384,   385,   385,   385,   385,   386,   386,   386,   386,   386,
     386,   386,   386,   387,   388,   388,   388,   389,   389,   390,
     390,   391,   392,   393,   393,   393,   393,   394,   395,   395,
     396,   397,   397,   397,   397,   398,   398,   399,   400,   401,
     401,   402,   403,   403,   403,   404,   406,   405,   407,   407,
     408,   405,   405,   409,   409,   410,   410,   411,   411,   413,
     414,   412,   412,   415,   415,   416,   416,   417,   417,   418,
     418,   418,   419,   420,   420,   420,   421,   422,   422,   423,
     424,   425,   425,   426,   427,   427,   427,   427,   427,   427,
     427,   427,   427,   427,   427,   427,   427,   427,   427,   427,
     428,   429,   430,   430,   431,   431,   432,   432,   433,   433,
     433,   435,   434,   436,   434,   434,   437,   437,   438,   440,
     439,   442,   441,   443,   444,   443,   445,   445,   447,   446,
     448,   449,   449,   450,   450,   451,   451,   452,   453,   453,
     454,   454,   455,   456,   456,   457,   457,   458,   459,   460,
     461,   461,   462,   462,   462,   462,   462,   462,   463,   463,
     463,   463,   464,   465,   465,   466,   467,   467,   468,   468,
     469,   469,   470,   470,   470,   470,   470,   471,   471,   472,
     473,   473,   474,   474,   476,   475,   475,   477,   477,   478,
     479,   479,   480,   481,   482,   482,   483,   484,   484,   485,
     485,   485,   485,   486,   486,   487,   487,   487,   488,   489,
     489,   489,   490,   491,   491,   492,   492,   493,   493,   493,
     494,   494,   495,   495,   496,   497,   497,   498,   499,   499,
     500,   500,   501,   501,   502,   503,   504,   505,   506,   507,
     507,   508,   508,   509,   510,   510,   511,   511,   511,   511,
     512,   512,   513,   513,   514,   515,   515,   516,   517,   519,
     518,   518,   520,   520,   521,   521,   522,   522,   522,   523,
     523,   523,   524,   524,   524,   525,   525,   525,   526,   526,
     526,   527,   527,   528,   529,   529,   529,   530,   530,   531,
     531,   532,   532,   533,   533,   534,   534,   534,   534,   534,
     535,   535,   536,   536,   537,   537,   538,   539,   539,   540,
     540,   541,   542,   543,   543,   543,   544,   544,   544,   545,
     545,   546,   546,   546,   547,   548,   548,   549,   550,   550,
     551,   551,   552,   553,   553,   554,   554,   555,   555,   555,
     556,   556,   557,   556,   556,   558,   558,   559,   561,   560,
     562,   563,   563,   563,   564,   564,   564,   565,   565,   566,
     566,   567,   567,   568,   568,   569,   569,   570,   571
};

/* YYR2[YYN] -- Number of symbols composing right hand side of rule YYN.  */
static const yytype_uint8 yyr2[] =
{
       0,     2,     1,     1,     1,     1,     1,     2,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     2,     0,     1,
       2,     1,     3,     2,     0,     2,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     3,     3,     1,     3,     4,
       7,     3,     0,     1,     0,     2,     0,     2,     1,     0,
       0,     4,     0,     0,     4,     3,     7,     5,     5,     0,
       1,     2,     1,     0,     2,     1,     5,     0,     7,     5,
       0,     1,     1,     2,     0,     2,     1,     5,     5,     3,
       0,     1,     2,     1,     0,     1,     2,     1,     2,     1,
       2,     3,     5,     5,     4,     0,     1,     2,     3,     0,
       2,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     2,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       4,     1,     1,     2,     0,     4,     0,     7,     0,     6,
       1,     2,     0,     3,     0,     2,     0,     2,     1,     1,
       1,     0,     9,     6,     0,     1,     1,     1,     2,     2,
       2,     0,     2,     1,     4,     3,     0,     2,     2,     1,
       1,     2,     7,     0,     2,     0,     1,     1,     0,     1,
       0,     1,     1,     1,     1,     1,     1,     4,     0,     2,
       2,     2,     1,     4,     3,     3,     0,     2,     2,     3,
       1,     1,     1,     1,     3,     1,     1,     1,     1,     2,
       1,     1,     1,     1,     1,     1,     1,     1,     3,     3,
       3,     3,     3,     3,     3,     3,     3,     3,     3,     3,
       3,     2,     1,     2,     2,     2,     2,     3,     4,     3,
       3,     3,     3,     3,     3,     3,     3,     3,     3,     3,
       3,     3,     3,     3,     3,     3,     3,     3,     2,     2,
       1,     3,     1,     1,     1,     1,     3,     1,     1,     1,
       1,     1,     2,     2,     3,     1,     1,     1,     1,     1,
       3,     1,     1,     1,     1,     1,     1,     1,     1,     3,
       1,     1,     1,     2,     2,     1,     3,     4,     3,     3,
       3,     2,     5,     4,     3,     5,     3,     4,     3,     2,
       0,     1,     0,     1,     3,     1,     2,     0,     2,     2,
       1,     1,     1,     4,     4,     3,     0,     2,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     4,     0,
       2,     2,     6,     2,     1,     0,     2,     1,     2,     4,
       7,     0,     2,     2,     3,     4,     0,     1,     6,     0,
       2,     1,     4,     2,     3,     5,     3,     2,     1,     0,
       1,     3,     2,     3,     0,     1,     2,     4,     0,     2,
       2,     1,     1,     1,     3,     1,     3,     1,     1,     6,
       0,     2,     7,     0,     2,     0,     1,     6,     7,     0,
       2,     1,     1,     1,     2,     1,     1,     1,     1,     7,
       6,     1,     1,     0,     2,     8,     6,     0,     4,     0,
       1,     7,     2,     1,     1,     0,     2,     2,     5,     7,
       4,     2,     1,     1,     0,     2,     2,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     6,     7,     0,
       3,     4,     0,     2,     4,     5,     1,     1,     1,     4,
       5,     2,     1,     0,     2,     1,     1,     1,     1,     1,
       1,     1,     1,     0,    15,     0,     1,     0,     2,     1,
       0,     4,     4,     0,     4,     0,     4,     0,     4,     0,
       1,     1,     3,     2,     1,     3,     1,     1,     1,     0,
       9,     0,     8,     8,     9,     0,     3,     3,     1,     4,
       2,     3,     1,     4,     2,     3,     1,     4,     2,     4,
       2,     3,     1,     5,     1,     3,     5,     2,     1,     0,
       2,     2,     2,     0,     2,     2,     1,     2,     1,     1,
       2,     0,     1,     3,     1,     0,     1,     8,     4,     0,
       2,     4,     0,     2,     1,     2,     0,     8,     4,     3,
       0,     5,     3,     1,     2,     2,     3,     3,     1,     0,
       0,    11,     4,     0,     3,     0,     1,     0,     1,     0,
       1,     2,     1,     0,     3,     3,     2,     0,     2,     2,
       1,     0,     2,     1,     1,     2,     1,     1,     1,     1,
       1,     1,     1,     1,     2,     1,     1,     1,     2,     1,
       5,     5,     0,     2,     0,     2,     0,     1,     0,     1,
       1,     0,    11,     0,    12,     6,     0,     2,     1,     0,
       5,     0,    10,     0,     0,     3,     0,     2,     0,     5,
       8,     0,     1,     0,     1,     0,     2,     5,     0,     2,
       0,     1,     5,     0,     2,     0,     1,     2,     2,     3,
       0,     1,     4,     5,     6,     5,     1,     1,     2,     1,
       2,     1,     2,     3,     4,     5,     0,     2,     0,     2,
       0,     2,     1,     3,     3,     3,     2,     0,     1,     7,
       0,     3,     0,     3,     0,     8,     5,     0,     2,     1,
       0,     2,     1,     1,     1,     1,     7,     0,     1,     0,
       3,     4,     3,     4,     7,     2,     4,     2,     3,     1,
       1,     1,     3,     0,     3,     0,     3,     2,     2,     1,
       1,     1,     1,     3,     8,     7,     7,     2,     0,     1,
       1,     3,     1,     1,     1,     1,     1,     1,     1,     4,
       4,     0,     2,     1,     1,     2,     1,     1,     1,     1,
       5,     5,     0,     2,     1,     2,     3,     1,     3,     0,
       8,     6,     1,     2,     1,     2,     1,     1,     1,     7,
       8,     6,     7,     8,     6,     0,     3,     2,     2,     1,
       3,     1,     2,     2,     9,     8,     8,     5,     4,     2,
       1,     2,     3,     1,     1,     7,     8,     6,     7,     4,
       4,     2,     9,     8,     1,     1,     8,     0,     1,     5,
       1,     4,     9,     0,     1,     1,     1,     3,     1,     5,
       4,     4,     5,     2,     7,     5,     1,     4,     1,     1,
       1,     1,     3,     6,     7,     1,     1,     0,     2,     2,
       0,     1,     0,     6,     4,     5,     6,     3,     0,     3,
       6,     1,     1,     1,     1,     1,     1,     3,     2,     3,
       2,     2,     3,     1,     4,     2,     3,     2,     1
};

/* YYDEFACT[STATE-NAME] -- Default rule to reduce with in state
   STATE-NUM when YYTABLE doesn't specify something else to do.  Zero
   means the default is an error.  */
static const yytype_uint16 yydefact[] =
{
       0,     0,   794,     0,     0,   172,   171,   626,     0,     2,
      24,     6,     0,     0,     0,     5,     4,   618,   619,     0,
     623,   796,   178,     8,     9,   174,     1,     7,     0,     0,
       0,     0,     0,     0,     0,    23,    25,    33,    34,    26,
       0,    29,     0,    27,     0,    28,     0,    30,     0,    31,
      32,     0,     0,     0,     0,     0,     0,   184,   186,     0,
       0,     0,     0,    21,     0,     0,     0,     0,     0,     0,
     322,   321,   328,   326,   325,   327,   324,   323,     0,     0,
       0,    49,     0,     0,     0,     0,     0,   908,     0,    85,
      89,     0,     0,   176,     0,   191,   633,   617,   622,   627,
     182,   189,   188,     0,     0,   180,   190,   175,     0,     0,
      41,     0,    35,     0,     0,    79,   911,   912,   913,     0,
       0,    36,     0,   335,     0,   334,   232,     0,   333,     0,
       0,   819,   732,     0,     0,     0,    50,    46,    59,     0,
       0,   513,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,   928,   112,    64,   102,   104,   134,
     123,    65,   111,     0,   124,   106,   107,   108,   130,   131,
     110,   109,   129,   126,   127,   125,   128,   133,   132,   103,
     105,   135,    70,     0,     0,     0,     0,   168,    75,    76,
     167,     0,   169,    80,     0,    80,     0,   147,    86,   138,
      87,     0,   143,   144,   142,   140,   141,   139,   146,   145,
     148,   149,     0,    88,    90,    95,    95,   100,   157,   150,
     101,   151,   152,   155,   156,   154,   153,   184,   194,   165,
     158,   203,   159,     0,   202,   162,   160,   161,   164,   163,
       0,   629,   624,   628,     0,   187,     0,   185,     0,   181,
       0,     0,    22,     0,    94,     0,     0,     0,     0,     0,
     914,   915,     0,   916,   328,   798,   332,   330,   329,   331,
       0,   797,   795,     0,    14,     0,   362,     0,     0,     0,
       0,     0,     0,   316,    13,   236,   243,   250,   251,   272,
     315,   321,   328,   317,     0,   318,   319,     0,   423,   245,
      12,    10,    11,     0,   231,   340,   338,   339,   336,     0,
     321,     0,     0,    42,    42,    42,   736,    39,    55,     0,
       0,    44,    62,     0,    60,   453,   451,   452,     0,     0,
       0,   737,     0,   464,   463,   465,     0,     0,   770,   771,
     769,   775,     0,     8,     0,     0,     0,     0,     0,     0,
       0,     0,   173,    72,     0,    71,     0,     0,   753,   750,
       0,     0,    70,    83,     0,    81,   909,     0,     0,    91,
       0,     0,    96,     0,     0,   196,   197,   195,     0,   641,
       0,   637,     0,   630,   620,   183,     0,   441,   445,     0,
       0,     0,   442,   446,   443,     0,     0,   206,     0,   221,
     210,   890,   891,   209,   179,    58,    69,     0,   835,   541,
     918,     0,     0,   927,   921,     0,     0,   920,     0,   234,
     350,   349,   235,   271,   274,   273,   276,   275,   360,     0,
       0,   357,   361,     0,   428,   427,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,   252,
     253,   254,   255,   256,   257,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,   335,   414,   412,
     341,     0,     0,    20,   337,   822,     0,     0,   827,   826,
     828,     0,   824,   734,    43,   733,   735,    54,     0,   513,
       0,    47,   122,   113,    48,   114,   118,   119,   117,   115,
     116,   121,   120,    61,    57,     0,     0,     0,     0,    59,
       0,     0,   626,     0,   588,   589,   514,   515,   516,   520,
     517,   518,   519,   566,     0,   572,   521,   522,   558,   562,
     327,   738,   742,     0,   462,     0,     0,     0,     0,     0,
     773,     0,     0,     0,   406,     0,   365,     0,     0,     0,
       0,     0,     0,     0,    73,    66,     0,   747,     0,     0,
      82,    77,    78,     0,   907,    97,    92,    93,     0,   200,
     199,   198,   193,     0,   695,   634,   636,   635,   166,   632,
       0,   631,   205,   895,   896,   900,     0,   900,   444,     0,
       0,     0,    21,   831,     0,     0,     0,     0,   925,   917,
       0,   409,     0,   408,   922,   919,   351,   352,   348,     0,
     320,     0,     0,   356,   233,     0,   237,   424,   268,   265,
     266,   269,   270,   267,   259,   264,   262,   261,   260,   263,
     297,   296,   295,   294,   293,   292,   286,   288,   285,   287,
     290,   289,   281,   284,   283,   280,   279,   291,   282,   258,
     277,     0,     0,     0,     0,     0,   300,   305,   311,   425,
     416,     0,   346,   415,   413,   411,   355,   344,     0,   244,
     823,   821,     0,     0,   825,    51,    45,     0,   454,     0,
     473,   472,   474,     0,   835,   321,     0,   664,     0,   568,
     570,   560,   564,   708,   595,     0,     0,   740,   430,     0,
     466,   321,     0,   327,     0,     0,     0,   779,     0,   765,
     775,   763,     0,   767,   768,     0,   435,     0,   363,     0,
       0,     0,     0,     0,     0,   367,   368,   370,   371,   372,
     373,   374,   375,   369,   376,   377,   364,   439,     0,     0,
     792,   793,     0,     0,   752,   744,   751,   170,    67,   248,
     242,     0,   241,   228,     0,   240,   247,   315,    98,   177,
     194,     0,     0,     0,   710,   730,     0,     0,   642,   643,
     659,   644,   646,   648,   649,   693,   647,   650,   651,   652,
     653,   655,   656,     0,     0,   657,   327,   717,   853,   854,
     716,   864,   865,   719,   721,     0,   638,   641,   901,   220,
       0,     0,   892,   900,   204,   220,   207,   218,   542,     0,
     837,   841,     0,   839,   829,     0,     0,   926,   333,   407,
     910,   353,   347,   278,   343,   354,     0,   358,   238,   298,
     299,   313,   312,   302,   303,   304,     0,     0,   308,   309,
     310,   307,     0,     0,   360,     0,   818,     0,    40,     0,
     835,   471,     0,     0,   468,    56,     0,   662,     0,   596,
       0,   591,   523,     0,   548,   546,   547,   626,   549,     0,
     565,   571,     0,     0,   557,   561,   612,     0,     0,     0,
       0,     0,   467,     0,   459,     0,     0,   776,   778,   777,
       0,   773,     0,     0,     0,   835,   447,   448,   433,   436,
     405,   403,     0,     0,     0,     0,   399,     0,     0,     0,
     379,    16,    15,     0,     0,     0,     0,   478,   481,   480,
     489,   477,   491,   486,   482,   479,   485,   487,   484,   483,
     492,   488,   490,   493,   495,   494,   788,   790,     0,     0,
     439,   746,     0,   754,     0,   748,   749,   755,    68,   249,
       0,     0,     0,     0,   667,     0,     0,   707,   711,     0,
       0,   728,     0,     0,     0,   700,     0,   705,     0,     0,
     694,   507,   508,     0,   666,   588,     0,   645,   654,   658,
     720,     0,   718,   722,   639,     0,     0,   206,     0,     0,
       0,   208,   225,   222,   224,   226,   223,     0,   219,   321,
     836,     0,     0,   842,   830,   924,   342,   360,   359,   301,
     426,   306,   314,   345,   354,   820,   450,     0,     0,   475,
       0,     0,   665,     0,     0,     0,   578,   583,     0,   574,
     579,   592,   594,     0,   590,   539,     0,     0,   567,   569,
     559,   563,     0,     0,   544,     0,     0,   506,     0,     0,
     496,   743,     0,     0,   431,   429,     0,   460,     0,     0,
     456,   766,   772,     0,   774,   834,     0,     0,     0,   321,
     391,   421,   418,   422,     0,   321,     0,     0,     0,     0,
       0,     0,   806,   808,   807,   802,   803,   809,     0,   385,
       0,   440,   437,     0,   789,   787,     0,     0,   782,     0,
     780,   781,     0,   759,   327,     0,   227,     0,   229,   239,
     192,     0,   671,   681,   709,   731,     0,   726,   666,     0,
     876,   873,   878,     0,   873,     0,   579,     0,   723,   698,
     701,     0,     0,   703,   706,   662,   511,   641,     0,     0,
       0,   623,   220,     0,   897,   905,     0,   215,   321,   843,
     449,   476,   469,   470,   663,   661,   595,     0,   586,   582,
     573,     0,   577,     0,   540,   537,     0,     0,     0,     0,
       0,   543,     0,     0,   602,     0,     0,   327,     0,   611,
       0,   741,   739,   461,     0,     0,   764,   832,     0,   434,
     432,     0,     0,     0,   395,     0,     0,   400,   401,   804,
     800,     0,     0,   817,   813,   814,   799,   378,     0,   380,
       0,   388,   786,   785,   791,     0,     0,   438,     0,   757,
     773,     0,   230,   668,     0,   641,   729,     0,     0,     0,
     859,   874,   875,     0,     0,     0,   712,     0,     0,   579,
       0,     0,     0,     0,   722,     0,     0,     0,     0,   673,
     724,     0,   880,     0,     0,   206,   904,     0,   893,   906,
     897,   216,   217,   213,   599,   584,     0,   585,   575,     0,
     580,   593,     0,   533,     0,     0,   850,     0,     0,     0,
       0,     0,     0,   604,   136,     0,   513,   602,     0,   613,
     615,     0,     0,     0,   455,   458,   833,   394,     0,     0,
     392,   417,     0,   419,   402,   396,   805,     0,   811,   810,
     381,     0,   386,   387,     0,   784,   783,     0,     0,   758,
       0,     0,   745,   669,     0,   670,     0,   686,   727,   725,
     588,   589,     0,     0,     0,   877,   715,     0,   713,     0,
     699,   697,     0,   321,   509,   704,   702,   660,     0,     0,
     883,     0,   879,   621,     0,   899,   898,   894,     0,   212,
       0,     0,   587,     0,   581,     0,     0,   530,     0,     0,
       0,     0,   849,     0,   555,     0,   545,   555,     0,   603,
     137,     0,   513,   502,   602,   614,   609,   616,     0,     0,
     321,   393,   420,   398,   397,   815,   384,   382,    18,     0,
     762,     0,   760,   675,     0,   676,   683,   867,     0,     0,
     714,     0,   857,     0,     0,   510,   691,     0,     0,     0,
     903,   214,   597,     0,   600,   576,   539,     0,     0,   527,
       0,   602,     0,     0,   851,     0,     0,     0,     0,   552,
       0,   553,   607,   605,   499,   502,   608,   390,     0,   816,
     383,     0,     0,    19,   761,     0,     0,     0,   684,     0,
       0,   687,   873,     0,   868,     0,   884,   886,     0,   861,
     855,   858,     0,     0,   692,   676,   881,     0,     0,   538,
     535,     0,     0,     0,   848,   602,     0,   852,     0,     0,
     550,   554,     0,     0,   497,   503,   499,   389,    17,   756,
     679,     0,   677,   678,   641,     0,     0,     0,     0,   866,
     870,     0,   863,     0,     0,   856,   690,     0,   882,   598,
       0,   534,     0,   513,   529,   528,   846,   847,   845,     0,
     556,   602,     0,     0,     0,     0,   498,   641,   668,   685,
     688,     0,   872,     0,     0,   862,   860,     0,   601,     0,
       0,   531,     0,   844,   500,   602,   602,     0,   680,     0,
     641,   682,     0,     0,   887,   668,     0,     0,     0,   501,
     504,   602,   672,   689,     0,   871,   885,     0,   536,     0,
     525,   505,   869,   674,   532,     0,   526,   524
};

/* YYDEFGOTO[NTERM-NUM].  */
static const yytype_int16 yydefgoto[] =
{
      -1,     8,     9,    10,    22,   283,   920,  1461,  1462,   284,
     340,    11,    12,    35,    36,   488,   155,    68,    39,    40,
     493,   500,   321,   501,   137,   320,    81,   135,    41,    42,
     323,    83,   156,    43,   958,    44,   354,    85,   188,   157,
      46,   364,    88,   198,    89,   158,    48,   371,    92,   217,
     159,   160,   504,  1294,  1295,   200,   220,   231,   589,   189,
      13,   162,   163,    58,   227,    57,   101,   102,   244,   103,
     104,   107,   164,   233,   378,    95,   234,   247,   600,   816,
     397,  1369,  1273,  1007,   398,  1008,   574,   960,  1118,   128,
     126,   433,   626,   763,   285,   764,   765,  1037,   474,   287,
     288,   665,   846,   666,   852,   667,   289,   290,    70,   310,
     292,   268,    73,   129,    74,   293,   294,   295,   296,   618,
     832,   429,   430,   623,   837,   431,   165,   559,   735,   736,
    1098,  1219,   737,  1407,  1220,  1322,  1099,  1323,   738,  1202,
    1310,  1080,   739,  1403,   740,  1087,  1207,   916,   741,   742,
     166,  1081,   829,   613,   674,   479,   913,  1203,  1313,  1082,
     432,  1083,   436,   167,   891,   168,  1078,   908,   169,   925,
     399,   909,   170,   325,   516,   171,   896,  1068,   172,   336,
     544,   710,   173,   174,   693,   694,   861,  1029,   946,  1059,
    1060,  1504,  1454,  1505,  1056,   980,   981,   982,   329,   330,
     526,   527,   528,  1045,  1595,  1492,  1535,  1439,  1561,  1377,
    1531,  1283,  1175,   408,  1055,  1181,   879,   529,  1052,  1053,
    1448,   530,   531,   532,   533,  1038,  1039,  1172,  1280,  1040,
    1169,  1277,   534,   870,  1044,   871,   535,  1370,  1371,  1434,
    1392,  1393,   536,   882,  1189,   883,  1301,  1190,    16,    17,
      53,   590,    55,    18,   242,   384,   591,   241,   382,   586,
     806,   583,   584,   778,   779,   780,   538,  1034,   867,   965,
    1334,   782,  1234,  1359,  1467,  1512,  1513,  1547,   783,  1235,
    1470,  1514,  1416,  1471,  1570,   784,  1483,   983,   785,   786,
    1251,  1139,   787,  1256,  1143,   788,   539,   790,   969,   791,
     792,   793,   794,   795,  1238,  1127,   971,   133,   542,   175,
     889,   707,   191,   954,   755,   955,   567,   756,   359,   956,
     957,  1330,  1229,   176,   549,   341,   342,   719,   723,   550,
     720,  1108,  1109,   177,   178,   947,  1105,   948,   749,    75,
     300,    76,   301,   302,   744,   919,  1095,  1210,  1096,   745,
    1090,  1214,  1318,  1215,   490,    49,   312,   486,   491,   492,
     179,   180,   606,   822,   823,  1013,   886,  1286,  1287,  1383,
     797,   798,  1423,   799,   800,   801,  1473,  1519,  1520,   802,
    1243,  1137,   803,  1263,   804,  1476,  1477,  1343,   400,   401,
     402,   595,  1268,   810,   996,   403,   214,   105,   194,    77,
     119,   259,   260,   261,   262,   411,   412,   263,   181
};

/* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
   STATE-NUM.  */
#define YYPACT_NINF -1373
static const yytype_int16 yypact[] =
{
    2220,   155, -1373,   859,   170, -1373, -1373, -1373,   118, -1373,
     350, -1373,   676,   289,   387, -1373, -1373, -1373, -1373,   296,
     109, -1373, -1373, -1373, -1373, -1373, -1373, -1373,   170,   170,
     170,   170,   821,   522,   170, -1373, -1373, -1373, -1373, -1373,
    2258, -1373,  1205, -1373,   681, -1373,  2325, -1373,  2358, -1373,
   -1373,   859,  2391,   401,   119,   402,   448,   436,   348,   542,
     587,   619,   650, -1373,   170,   170,   627,   472,   702,   492,
     392,   392,   736, -1373,   549, -1373, -1373, -1373,   664,   684,
     103,   700,   788,  1537,   871,   617,   892, -1373,  2463, -1373,
      68,   937,  2535, -1373,   948,  2601,   622, -1373, -1373,   170,
   -1373,   523, -1373,   727,   922,   907, -1373, -1373,   170,   170,
   -1373,   170, -1373,   949,   983,   858, -1373, -1373, -1373,   740,
     522, -1373,   865, -1373,   959, -1373,   882,   421, -1373,   468,
    1924,  1001,   782,   885,   896,   727, -1373, -1373,   629,   859,
     170, -1373,   170,   170,   455,   170,   691,   859,   170,   263,
     787,   170,   170,   977, -1373, -1373, -1373, -1373, -1373, -1373,
   -1373, -1373, -1373,    75, -1373, -1373, -1373, -1373, -1373, -1373,
   -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373,
   -1373, -1373,   686,   170,   199,   375,   170, -1373, -1373, -1373,
   -1373,  1036, -1373,   295,   727,   295,   813, -1373, -1373, -1373,
   -1373,   918, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373,
   -1373, -1373,  1035, -1373,   932,   566,   566, -1373, -1373, -1373,
   -1373, -1373, -1373, -1373, -1373, -1373, -1373,   436,   666, -1373,
   -1373, -1373, -1373,  1069, -1373, -1373, -1373, -1373, -1373, -1373,
     495,  1046, -1373, -1373,   727, -1373,  1218, -1373,   522, -1373,
    1055,  1065, -1373,  1059, -1373,   170,   170,   170,   958,   953,
   -1373, -1373,    76, -1373,   793, -1373, -1373, -1373, -1373, -1373,
     967, -1373, -1373,   522, -1373,   968, -1373,   840,   840,   840,
     840,   840,  1588, -1373, -1373, -1373,   114,   596,  1985,   972,
     492,   388, -1373, -1373,   802, -1373, -1373,   969, -1373,   970,
   -1373,   170, -1373,  1588, -1373, -1373, -1373, -1373, -1373,   976,
     810,  1027,   488,   170,   170,   170, -1373, -1373, -1373,   978,
     727,  2502,   170,   979, -1373,   981, -1373, -1373,   117,  1098,
     628,  1087,   293, -1373, -1373,   492,   984,   418, -1373, -1373,
     985,   486,   988,  1094,   660,   990,  1095,   991,   111,   724,
      71,   170, -1373,   170,   992, -1373,  1093,  1110,   492, -1373,
     170,   996,   686,   170,   998, -1373, -1373,   999,  1005, -1373,
    1129,  1002, -1373,  1003,  1086,   966,   170, -1373,  1008, -1373,
    1017,   492,  1018, -1373,  2601, -1373,  1024, -1373, -1373,   859,
     170,   170, -1373, -1373, -1373,  1092,  1139, -1373,   170, -1373,
   -1373, -1373, -1373, -1373,   582, -1373, -1373,  1025,   531, -1373,
      45,  1019,   170,  1026, -1373,   522,  1028,    45,   170, -1373,
     240, -1373, -1373, -1373,  1038, -1373, -1373, -1373,   271,  1037,
    1034, -1373, -1373,   836, -1373, -1373,  1924,  1924,  1924,  1924,
    1924,  1924,  1924,  1924,  1924,  1924,  1924,  1924,  1924, -1373,
   -1373, -1373, -1373, -1373, -1373,  1924,  1924,  1924,  1924,  1924,
    1924,  1924,  1924,  1924,  1924,  1924,  1924,  1924,  1924,  1924,
    1924,  1924,  1924,  1924,  1924,   840,  1955,  1044,   507, -1373,
   -1373,  1588,  1924, -1373, -1373,   170,  1033,   522, -1373, -1373,
   -1373,   655, -1373, -1373, -1373, -1373, -1373, -1373,  1040, -1373,
     684, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373,
   -1373, -1373, -1373, -1373, -1373,   522,  1151,   773,   522,   629,
    1924,  1924,   431,  1588,   685, -1373, -1373, -1373, -1373, -1373,
   -1373, -1373, -1373, -1373,  1064, -1373, -1373, -1373, -1373, -1373,
    1049, -1373,  1161,   522,  1052,   522,   522,  1155,   297,  1051,
    1144,  1924,  1159,   522, -1373,   522, -1373,  1058,   408,  1060,
     522,  1070,   859,   783, -1373, -1373,  1177,  1138,   809,  1072,
   -1373, -1373, -1373,  1317, -1373,   170, -1373, -1373,   522, -1373,
   -1373, -1373, -1373,  1193,   888, -1373,  1078, -1373, -1373, -1373,
    1212, -1373, -1373, -1373, -1373,   561,  1192,   561, -1373,   859,
     712,   900,   739, -1373,   170,   162,   124,  1924, -1373,    45,
    1079,   280,  1116, -1373, -1373,    45,   615,   582, -1373,   840,
   -1373,  1588,  1924,  1082, -1373,  1588, -1373, -1373,  2607,  2607,
    2607,  2607,  2607,  2607,  2607,  2607,  2607,  2607,  2607,  2607,
    2627,  2627,  2627,  2627,  2627,  2627,  2627,  2627,  2627,  2627,
    2627,  2627,   774,   774,   774, -1373, -1373, -1373, -1373,  2607,
   -1373,   672,   672,   840,   840,   471, -1373,   841,  1096,  1023,
   -1373,  1588, -1373,   615, -1373, -1373,   216, -1373,  1089, -1373,
   -1373, -1373,   843,  1027, -1373, -1373, -1373,  1097, -1373,   367,
   -1373, -1373, -1373,  1203,  1090,   679,  1100,  1180,  1179, -1373,
   -1373, -1373, -1373, -1373,  1216,  1852,   727,  1196,  1108,   522,
   -1373,   147,  1217,   254,  1005,   522,   522, -1373,   170, -1373,
    1223,  1233,  1221, -1373, -1373,   822,   413,  1113, -1373,   522,
    1122,  1219,   170,   777,   986, -1373, -1373, -1373, -1373, -1373,
   -1373, -1373, -1373,  1194, -1373, -1373, -1373,  1128,  2126,  1134,
   -1373, -1373,   522,  1130, -1373,  1252, -1373, -1373, -1373, -1373,
   -1373,  1924, -1373, -1373,  1142, -1373, -1373,   844, -1373,   582,
     666,   877,  1924,  1146,  1924,  1242,  1924,   433, -1373, -1373,
   -1373, -1373, -1373, -1373, -1373,   827, -1373, -1373, -1373, -1373,
   -1373, -1373, -1373,   320,   194, -1373,  1150, -1373, -1373, -1373,
   -1373, -1373, -1373, -1373, -1373,   522, -1373, -1373,  1158,  1708,
    1235,  1257, -1373,   561, -1373,  1708, -1373,   880, -1373,   522,
   -1373,   582,  1147,    59, -1373,  1157,  1162, -1373,  1047, -1373,
   -1373,   615, -1373, -1373, -1373,  1163,  1588, -1373, -1373, -1373,
   -1373, -1373, -1373, -1373, -1373, -1373,   672,  1955, -1373, -1373,
   -1373, -1373,   672,   840,   351,  1924, -1373,  1166, -1373,  1173,
     779,  1164,  1924,  1178, -1373, -1373,  1924,  1245,   173, -1373,
    1918,   466, -1373,  1388, -1373, -1373, -1373,   431,   532,   170,
   -1373, -1373,  1294,  1291, -1373, -1373, -1373,  1181,   727,   684,
    1924,  1183,   492,  1924,   880,  1924,  1184, -1373,   582,   492,
     854,  1144,  1297,  1005,  1185,   531, -1373, -1373,  1182, -1373,
   -1373, -1373,  1955,  1285,   522,   914, -1373,  1309,  2424,   696,
   -1373, -1373, -1373,   170,  1924,  1188,  1190, -1373, -1373, -1373,
   -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373,
   -1373, -1373, -1373, -1373, -1373, -1373,  1191, -1373,   853,   986,
    1128, -1373,   463, -1373,  1314, -1373, -1373, -1373, -1373, -1373,
     861,  1858,  1195,  1318, -1373,  1924,  1277, -1373, -1373,  1198,
     522,  1275,  1286,  1845,  1924,   170,   170,   170,  1924,  1924,
   -1373, -1373, -1373,  1307,  1200,   492,   544, -1373, -1373, -1373,
   -1373,  1924, -1373,   751,   492,  1325,  1211, -1373,   522,   170,
    1292, -1373, -1373, -1373, -1373, -1373, -1373,   522, -1373,   626,
   -1373,   522,   522, -1373, -1373, -1373, -1373,   306, -1373, -1373,
     606, -1373, -1373, -1373, -1373, -1373, -1373,  1210,   859, -1373,
    1213,  2139, -1373,  1924,  1222,  1234, -1373,   470,  1224,  1282,
   -1373, -1373, -1373,  1924, -1373,  1326,  1334,  1335, -1373, -1373,
   -1373, -1373,  1336,  1315,   446,   589,  1341, -1373,  1924,  1348,
   -1373, -1373,  1227,  1228, -1373, -1373,  1230, -1373,  1924,  1342,
   -1373, -1373, -1373,  1231, -1373, -1373,   154,  1924,  1236,   518,
   -1373, -1373, -1373, -1373,   522,   784,   522,   438,  1288,  1355,
    2568,  1288, -1373, -1373, -1373, -1373, -1373, -1373,   870, -1373,
    1238, -1373, -1373,  1249, -1373, -1373,  1246,  2139, -1373,   894,
   -1373, -1373,  1247,   324,   920,  1367, -1373,  1317, -1373, -1373,
   -1373,  1380, -1373, -1373, -1373, -1373,  1924,  1369,  1200,  1255,
   -1373,   291, -1373,  1924,   291,  1259,  1323,  1918, -1373,  1331,
   -1373,   170,    66,  1332, -1373,  1245, -1373, -1373,  1924,  1924,
      79,   109,  1708,   789,   362,   152,   522,  1032,   689,   582,
   -1373, -1373, -1373, -1373, -1373, -1373,  1216,  1924,  1406, -1373,
   -1373,  1924,  1268,  1313, -1373,  1273,  1420,  1343,  1376,  1378,
     170, -1373,  1382,  1383,  1594,  1391,  1924,  1283,   128, -1373,
    1405, -1373, -1373, -1373,  1287,  1924, -1373, -1373,  1290, -1373,
   -1373,   484,   926,   928, -1373,  1293,  1379, -1373, -1373,   170,
   -1373,  1338,  1338, -1373, -1373, -1373, -1373, -1373,   986, -1373,
     653, -1373, -1373, -1373, -1373,  1295,   986, -1373,   506,  1410,
    1144,  1299, -1373,    84,  1407, -1373, -1373,  1924,  1303,   173,
   -1373, -1373, -1373,  1924,  1352,  1306, -1373,  1924,  1316,  1384,
    1924,  1320,  1426,  1955, -1373,  1924,  1322,  1324,  1437, -1373,
   -1373,  1924, -1373,    82,  1327, -1373, -1373,    85, -1373, -1373,
     362, -1373, -1373,  1330, -1373, -1373,  1924, -1373,  1442,  1924,
   -1373, -1373,  1924,  1438,  1447,  1588, -1373,    94,  1005,  1005,
    1333,  1005,  1005, -1373, -1373,  1651, -1373,  1594,  1446,   170,
   -1373,  1337,  1339,  1449, -1373, -1373, -1373, -1373,  1430,   522,
   -1373, -1373,  1955, -1373, -1373,   170, -1373,  1469, -1373, -1373,
   -1373,  1414, -1373, -1373,  1359, -1373, -1373,   170,  1345, -1373,
    1463,  1346, -1373, -1373,  1347, -1373,  1419, -1373, -1373, -1373,
     692,  1354,  1365,  1356,   257, -1373, -1373,    88, -1373,  1924,
   -1373, -1373,  1955,   334, -1373, -1373, -1373, -1373,  1459,  1466,
    1429,  1924, -1373, -1373,   795, -1373, -1373, -1373,  1924, -1373,
    1360,  1918, -1373,  1918, -1373,  1363,   727,  1453,  1479,  1364,
    1588,   148, -1373,  1486,  1458,  1375, -1373,  1458,  1377, -1373,
   -1373,  1348, -1373, -1373,  1594, -1373, -1373, -1373,  1385,   522,
     358, -1373, -1373, -1373, -1373,   170,   170, -1373,  1518,   874,
   -1373,  1494, -1373, -1373,  1588, -1373,  1062,   648,  1924,  1924,
   -1373,  1924, -1373,  1387,   106, -1373,   170,  1419,  1924,  1455,
   -1373, -1373, -1373,  1460, -1373, -1373,  1326,  1390,   727, -1373,
    1449,  1594,  1381,  1449, -1373,  1393,  1504,  1496,  1397, -1373,
    1398, -1373, -1373, -1373,  1071, -1373, -1373, -1373,  1402, -1373,
   -1373,  1399,   170, -1373, -1373,  1409,  1404,   327, -1373,  1924,
    1528, -1373,   291,  1924, -1373,  1483, -1373, -1373,   131,  1485,
   -1373, -1373,  1415,  1417, -1373, -1373, -1373,  1924,  1588, -1373,
    1536,  1422,  1756,  1424, -1373,  1594,  1425, -1373,  1449,  1005,
   -1373, -1373,   158,  1924, -1373, -1373,  1071, -1373, -1373, -1373,
   -1373,  1556, -1373, -1373, -1373,  1507,  1541,  1924,  1500, -1373,
   -1373,  1588, -1373,   139,  1924, -1373, -1373,   528, -1373,  1433,
    1538, -1373,  1523, -1373, -1373, -1373, -1373, -1373, -1373,  1432,
   -1373,  1594,  1550,  1283,  1552,  1924, -1373, -1373,    84, -1373,
   -1373,  1445, -1373,  1588,   881, -1373, -1373,  1567, -1373,  1005,
    1553, -1373,  1569, -1373, -1373,  1594,  1594,  1565, -1373,  1450,
   -1373, -1373,   887,  1924, -1373,    84,  1451,  1005,  1587, -1373,
   -1373,  1594, -1373, -1373,  1924, -1373, -1373,  1454, -1373,  1457,
     170, -1373, -1373, -1373, -1373,  1461, -1373, -1373
};

/* YYPGOTO[NTERM-NUM].  */
static const yytype_int16 yypgoto[] =
{
   -1373, -1373, -1373, -1373,   -40, -1373,   396, -1373, -1373, -1373,
     -15,  1608, -1373, -1373, -1373,  1609,     5,  1148, -1373, -1373,
     796, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373,
    1106, -1373, -1373, -1373, -1373, -1373,  1269, -1373, -1373,   125,
   -1373,  1439, -1373, -1373, -1373,  1621, -1373,  1423, -1373, -1373,
     -85,   -73, -1373,   -82, -1373, -1373, -1373,  -345, -1373, -1373,
    -219,   717,     9, -1373, -1373, -1373, -1373,  1540, -1373, -1373,
      38, -1373,     8, -1373,   868, -1373, -1373,  -121,  -918, -1373,
    -735, -1373, -1373, -1373, -1373,   748,  -691, -1373, -1373,   -64,
    1517, -1373, -1373,   530,  1029, -1373,   687,   283, -1373, -1373,
    2254,   805, -1373,  -573, -1373,   797,  -231,   941, -1373,    34,
      24, -1373, -1373,  1589,  -455,  -245, -1373,  1389, -1373, -1373,
   -1373,  -367,  -461, -1373, -1373,   -99, -1373, -1373, -1373, -1373,
   -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373,
   -1373,   349, -1373, -1373, -1373, -1373, -1373,   573, -1373, -1373,
   -1373,  -294, -1373,   -80, -1373,  -347, -1373, -1373, -1373, -1138,
     -66,  -450,  1006, -1373, -1373,   -11, -1373, -1373,   -36,   716,
   -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373,    -2, -1373,
   -1373, -1373,   -26,   -52, -1373, -1373, -1373, -1373,   644, -1373,
   -1373,   172,   227, -1373, -1373, -1373,   806, -1373,  -486, -1373,
   -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373,
   -1373, -1373,   251,  -233, -1373, -1373, -1373, -1373, -1373, -1373,
     302, -1373, -1373, -1373,  -454,   317,  -912, -1373, -1373,  -913,
   -1373, -1373,  -503,   525, -1373, -1373,  -452, -1373, -1373, -1373,
     508, -1269, -1373, -1373,   303, -1373, -1210, -1373,  1366,   -13,
   -1373, -1373,   547,   -18, -1373, -1373, -1373, -1373,   725, -1373,
   -1373,  -770, -1373, -1373, -1373, -1373,  -421,   555, -1373,  -886,
   -1372, -1373, -1373, -1373,   218, -1373, -1217, -1373, -1373, -1373,
   -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373, -1373,
   -1373, -1373, -1373, -1373, -1373, -1373,  -427, -1373, -1373,   912,
   -1373,  -892, -1373,   913, -1373, -1373, -1373,  -437, -1373,  1620,
   -1373, -1373,  -702, -1373, -1373, -1373, -1373, -1373, -1373, -1373,
   -1373, -1373, -1373, -1373, -1373,   761, -1373,   487,  -825,  -665,
   -1373,   490, -1373,   -50,   -17,   607, -1373, -1373, -1373,     0,
   -1373,    51,   309,  -117, -1373, -1373, -1373,   634, -1373, -1373,
   -1373, -1373,   515, -1373, -1373, -1373, -1373,  1045, -1373,  1229,
     150,   -70,  -600, -1373, -1373, -1373, -1373,   432, -1373, -1373,
   -1373, -1373,   294, -1373, -1373, -1373, -1373,   160, -1373, -1373,
   -1062,   322, -1373,   264,   952, -1162, -1373, -1373, -1373, -1373,
   -1373,  1149,   477,  -510, -1373, -1373,   -93,  1703, -1373, -1373,
   -1373, -1373, -1373, -1373, -1373,   440,  1488, -1373, -1373
};

/* YYTABLE[YYPACT[STATE-NUM]].  What to do in state STATE-NUM.  If
   positive, shift that token.  If negative, reduce the rule which
   number is the opposite.  If zero, do what YYDEFACT says.
   If YYTABLE_NINF, syntax error.  */
#define YYTABLE_NINF -924
static const yytype_int16 yytable[] =
{
      19,   161,    56,    23,    25,   269,   125,   218,    15,    14,
     229,    93,   249,   686,   319,   199,    62,    38,   211,   219,
     678,   669,   230,   897,   413,   297,   670,   396,    59,    60,
      61,    63,    66,   190,    78,   192,   206,   995,   208,   588,
     224,    97,   225,   237,   298,   238,   423,   424,   425,   426,
     427,    23,   203,   953,    24,   901,   222,    72,   299,   235,
    1136,  1135,   205,   687,   113,   114,   223,    71,   699,   236,
     700,   209,  1245,   366,   997,   226,  1072,   202,   239,  1153,
    1001,   777,   267,     2,  1141,   525,   204,   812,   839,   840,
     187,     2,     2,  1398,   863,   702,   106,   201,  1148,   243,
     221,   701,    24,   232,   134,   212,  1253,  1261,   250,   251,
    1361,   252,   557,   561,   677,  1354,  1421,    52,    26,  1415,
       1,   612,    23,   385,  1381,  1455,  1011,   306,   332,   308,
     337,   675,   316,   344,  1421,     2,   349,    45,   324,   326,
     328,   434,   331,    63,   264,    63,    63,   343,    63,   346,
     348,    63,   350,   558,    71,     2,   893,   789,   291,  1261,
     678,   212,    50,   781,  1299,     2,  1186,  1361,    21,     2,
     517,  -625,  1494,    24,  1402,  1285,  1569,     2,    71,    21,
       2,  -625,   355,   356,  1443,    20,   361,   435,   377,   212,
     327,   212,   607,   365,  1541,   365,    66,     7,    24,   498,
     357,  -923,   298,  1587,   297,    21,     2,     7,  1012,    72,
    1485,   743,  1074,   197,  1425,   372,   372,  1254,    71,    71,
     213,   688,   562,   298,  1249,  1248,  1537,   352,    23,   819,
    1493,  1262,   672,  1496,  1362,   416,   502,   299,   210,  1365,
    1422,  1333,  1239,   434,   660,   668,    21,     2,   503,   708,
     -53,   880,   712,   881,   834,   409,   410,   409,  1481,   726,
    1027,   727,   417,  -366,   345,   374,   747,  -360,   518,   510,
       2,   511,  1564,  1019,    71,   991,   824,   525,   885,    24,
    1300,    67,   404,  1522,   884,   507,    21,     2,  1539,   435,
     986,  1555,    67,   123,   124,   509,  1579,  1580,   434,   229,
    1444,   483,     2,  1000,   512,  1076,  1197,   420,   604,   895,
     506,   230,  1591,   494,   494,   494,   291,   489,    67,   508,
     523,   715,   513,   820,    51,   478,    21,     2,   716,   505,
     540,  1241,   237,   434,   238,   520,   563,   291,  1419,   525,
      21,     2,   984,   476,   435,   568,   992,  1364,   235,  1242,
      -3,    63,   717,   564,   363,  1552,   616,  1511,   236,    67,
      63,   547,   355,   570,    71,  1035,  1374,   239,   859,  -360,
    -409,  -360,   987,    21,     2,   580,   581,  1258,   434,   435,
    -409,    21,     2,   601,  -775,    87,   477,   124,   833,   593,
     596,   597,   232,  1380,    21,     2,  -321,   476,   602,    67,
     775,   298,  1228,   776,  1267,  1331,  -457,   286,  1414,  1420,
    1517,  1586,   609,   309,   673,   718,   729,  1265,   615,   620,
    -355,  1458,   730,  1048,   435,  1049,   123,   124,     2,    52,
     668,   668,   841,   842,   906,   911,  -321,    21,     2,    67,
     594,   731,   111,   298,   543,     2,   520,    54,  1230,   611,
    1051,   476,  1063,    67,   617,   100,  1050,   669,   950,  1433,
     525,    21,     2,    96,   357,  1337,   333,   523,  1206,    21,
       2,   476,   732,    87,   338,     2,  -775,   692,   907,  1167,
     477,   124,  -409,  -409,   305,   680,    67,   271,   265,   272,
      21,     2,   905,   360,    67,  -184,   489,   116,   434,  1023,
    -355,    21,     2,  -186,   123,   124,   380,    67,   123,   124,
      99,    72,   334,   825,   521,   291,   733,    23,  -321,   324,
     339,    71,  1168,   547,    21,     2,   297,  1329,    21,     2,
     715,   307,    31,   273,   477,   124,   274,   716,   123,   124,
    1041,   298,   117,   100,   435,   298,   713,   828,   525,   611,
      67,   118,   695,   831,    98,   734,    71,   291,  1557,   299,
    1518,   717,   750,   973,   548,   428,    33,   111,    24,   546,
     476,  1042,   754,     2,    67,   768,  1043,   611,   523,   711,
     611,  1201,    67,   974,   796,   887,   286,   611,   487,   611,
     396,   298,  -551,  1180,   611,   108,   396,  1384,  1385,   593,
    1387,  1388,   604,    67,   818,   843,   844,   845,   437,  1414,
    1542,  1545,   769,   751,    67,   668,   668,   922,    71,   661,
     662,   668,  1022,   525,  1327,   370,  1182,   663,   664,     1,
     100,   282,   525,   183,    21,     2,     2,    67,  1307,   821,
     109,    67,   322,   520,   438,   478,  1054,   439,   122,  1183,
     594,   184,   440,   123,   124,   291,  -512,  -512,  -512,   291,
       2,   110,  -588,  -321,   477,   124,  -409,  -409,   808,   115,
    -184,  1518,    21,     2,   973,   271,   265,   272,    21,     2,
     441,   668,    84,  1321,   703,   683,   442,   604,   122,    28,
    -625,   605,    97,     2,  1149,   185,   130,   -74,     2,    31,
      29,   375,   338,   900,   186,   291,   131,    30,   809,  -512,
     353,   521,   183,  1157,   132,   -74,   522,   915,    63,   627,
      31,   273,  1130,    71,   274,   409,  1091,   376,   123,   124,
     377,   147,    63,    33,   921,    32,  1342,  1018,  -321,    71,
     843,   844,   845,    71,  1549,  1213,   669,    67,   339,   898,
      71,   670,  1472,  1132,    33,   487,   298,     4,  1133,   -74,
     136,  -333,  -333,   611,   676,   679,  1155,  1062,   -74,   240,
      23,  -333,   123,   124,    33,   523,    34,  1568,   917,    21,
       2,     5,     6,  -696,   690,  -696,   611,  -838,   347,     2,
    1204,    67,  1205,  -801,     2,   313,   918,  -696,   669,   111,
    1583,  -696,   112,   697,   698,   314,   676,  -801,  1540,   111,
    1391,   553,  -801,   315,    64,   663,   664,  -696,   138,   282,
       2,    24,    64,   904,  1379,   123,   124,    71,     2,     2,
     691,   864,  1111,  -696,   724,   123,   124,   703,  -801,    71,
      65,   122,  -888,   271,   265,   272,    21,     2,   122,  1097,
    -840,   120,   610,  1009,   121,  -801,   766,   669,   610,   975,
     814,   976,  -801,  -801,   815,    21,     2,  1094,  1576,   265,
     291,    21,     2,   111,   246,   560,   266,   977,   963,   409,
    -666,  -666,  -666,  -666,  -666,   -37,  1589,  -211,   -37,   273,
     826,  -211,   274,   978,    21,     2,   256,   669,   257,   258,
    1002,   182,    71,   520,   676,   835,  1453,   255,   286,   979,
     771,    71,   470,   471,   472,   473,  -640,  -640,  -640,  1442,
    1003,  1004,   193,  1100,  1092,  1005,  -666,   772,   201,  -666,
     123,   124,   111,   396,   752,   122,  -404,  1266,  1006,   605,
     773,   815,   -38,  1430,   212,   -38,  1079,   815,  1085,  1110,
     480,   481,  1114,  1466,   854,   774,   477,   124,   111,    67,
     270,   757,   271,   265,   272,    21,     2,   215,   775,  -640,
     668,   776,   579,     2,    69,  1140,  1142,  1144,   228,   848,
     849,   850,   851,  1198,   624,   625,    71,   282,  1161,   248,
     265,   253,   120,     2,  1341,   856,  -666,  -246,  -666,   409,
     122,  1106,  1107,   111,    71,   229,  1071,    67,   273,  1116,
    1117,   274,  -666,  -666,   275,   828,   276,   230,  1217,  1218,
    -666,  -666,   668,   111,  -666,   254,  1464,  1529,    23,   303,
    1573,   311,  1154,  1574,   964,   523,  1584,   317,   237,  1585,
     238,   611,  1225,  1226,   959,  1158,  1159,  1562,   318,   111,
    -325,   817,  1271,  1272,   235,   966,   351,   968,  1187,   972,
    1554,    69,  1269,   111,   236,  1086,   362,  -410,  -410,   -21,
     352,   -21,   915,   239,  1308,  1309,  1311,  1312,    67,    24,
     277,   668,   368,  -410,   369,   335,   379,    63,   383,  -410,
    1468,  1469,  1572,  -410,   278,   279,  -325,   405,   232,  1502,
    1503,   922,   280,   281,   415,  1457,   282,   406,   407,  1111,
     495,   496,  -410,   478,   414,   419,   422,   475,   611,  1017,
     611,   668,   482,  -361,   484,   358,    69,   485,   519,   541,
     497,   514,   515,    56,   111,   545,   552,   555,  1024,   551,
     566,  1252,   554,   556,   565,  1030,   517,   562,   575,  1032,
     571,   572,   573,   578,   576,   577,  1047,  -325,  -325,  -325,
     582,  -325,  -325,  -325,  -325,   585,   587,  -410,  -325,  -325,
     130,   598,   592,  1064,   599,   608,  1066,   603,  1069,  -325,
    1290,   381,   604,   619,   614,   681,   621,   622,  1302,    69,
    1270,   671,   685,   689,   704,  -410,  -410,  -410,   706,  -410,
     705,   709,   714,   721,   722,   298,    82,  1101,   725,  1316,
     728,   753,   746,  1390,    69,   -63,    33,   748,   921,   386,
    1324,   -63,   -63,   770,   758,  -220,  1110,   805,   -63,   807,
     -63,   -63,    69,  1335,   811,   827,   830,   836,   -63,   -63,
     -63,   853,   855,   387,   766,   862,   866,   868,  1122,   858,
     605,   388,   865,   389,   869,  1437,   888,  1138,   890,   894,
     547,  1145,  1146,   902,   -63,   910,   -63,  1366,   903,   912,
     923,   524,   914,    71,  1150,   -63,   -63,   390,   924,   391,
     -63,   949,   951,   -63,   -63,  1187,   952,  1353,   392,   828,
     -63,   -63,   -63,   393,   -63,   961,   970,   394,   967,  1395,
     298,   993,   998,     5,     6,  -902,   999,   395,  1010,  1014,
    1015,  1016,  1409,  1028,  1033,  1404,  1164,  1491,  1025,   291,
     271,   265,   272,    21,     2,  1026,  1173,    63,   976,  1031,
    1058,  1073,  1077,  1061,   298,  1065,  1070,  1075,  1084,  1088,
    1102,  1185,  1103,  1400,  1115,  1104,  1353,  1120,  1121,  1123,
    1124,  1194,  1126,  1147,  1128,  1151,    69,   964,  1152,  1156,
    1199,    69,  1160,  1171,  1166,  1162,   273,   -63,  1174,   274,
    1176,  1177,   759,  1178,  1165,  1179,  1170,  1184,  1188,  1191,
    1192,  1445,  1193,  1196,  1195,  1211,  1353,   478,  1200,  1046,
    1221,   271,   265,   272,    21,     2,  1209,  1222,  1223,  1227,
     766,  1231,  1233,  1237,  1247,  1459,  1460,  1240,   298,  1236,
    1534,  1246,  1250,  1255,   291,  1276,  1244,  1279,  1281,   760,
    1282,  1284,   761,  1288,  1285,  1289,  1484,  1297,    69,  1291,
    1292,  1259,  1260,   611,  1254,  1303,    67,   273,   277,  1304,
     274,   298,  1306,  1315,   184,  1314,  1317,  1325,   291,  1336,
    1275,  1332,   278,   279,  1278,  1339,    69,  1345,  1346,    69,
     280,   281,  1508,   524,   282,  1349,  1352,  1358,  1348,  1298,
    1373,   762,  1351,   298,  1356,  1376,  1357,  1378,  1305,  1363,
    1368,  1386,  1394,  1399,    69,  1299,    69,    69,  1405,  1396,
    1406,  1397,  1408,  1411,    69,  1417,    69,  1410,  1412,  1413,
    1414,    69,  1543,  1187,  -889,  1426,  1418,    67,  1427,   277,
    1428,  1436,  1432,  1438,   767,  1440,  1446,  1441,  1447,    69,
    1338,   271,   291,   278,   279,   524,  1344,  1449,  1465,  1451,
    1347,   280,   281,  1350,  1495,   282,  1487,  1456,  1355,  1480,
    1498,  1488,  1490,  1499,  1360,  1497,    69,   139,  1335,  1500,
    1501,  1507,    69,   140,   141,   291,  1307,  1510,  1516,  1372,
     142,  1509,   143,   144,  1521,  1375,  1524,  1525,  1017,  1526,
     145,   146,   147,  1530,  1532,  1335,  1536,  1538,  1548,  1550,
    1551,  1553,  1558,  1560,  1563,  1559,  1565,   291,  1566,  1575,
    1596,   271,   265,   272,    21,     2,    32,  1571,     4,  1578,
    1577,  1581,  1582,  1588,   139,  1590,  1593,   148,   149,  1594,
     140,  1293,   150,  1597,  1320,    33,   151,   142,    27,   143,
     144,    37,     5,     6,   152,   696,   153,   145,   146,   147,
     860,   569,  1424,    47,   367,   682,  1093,   273,   962,   373,
     274,   245,  1067,   304,  1429,   276,   878,  1232,  1119,  1021,
     892,  1431,  1020,    32,   838,     4,    69,   899,  1401,   127,
    1208,   139,   421,  1017,   148,   149,  1112,   140,  1389,   150,
      69,   847,    33,   151,   142,  1163,   143,   144,  1546,     5,
       6,   152,  1506,   153,   145,   146,   147,  1489,  1057,  1450,
    1435,  1274,  1296,    69,  1452,  1125,   537,  1017,  1264,   154,
    1257,  1475,  1478,  1527,  1479,   988,   989,    67,   207,   277,
      32,  1486,     4,  1113,  1224,  1328,  1326,  1463,  1482,  1382,
     684,   148,   149,   278,   279,  1216,   150,  1319,   857,    33,
     151,   280,   281,   387,   985,   282,     5,     6,   152,  1474,
     153,   388,  1523,   389,  1592,   990,   994,  1367,   813,    90,
     418,     0,  1515,     0,     0,     0,   154,     0,     0,     0,
      69,     0,     0,     0,     0,     0,   139,   390,     0,   391,
    1528,  1017,   140,  1533,     0,     0,     0,     0,   392,   142,
       0,   143,   144,   393,     0,     0,  1544,   394,     0,   145,
     146,   147,     0,     5,     6,     0,     0,   395,     0,     0,
    1475,     0,     0,     0,  1017,     0,     0,  1556,     0,   985,
       0,     0,     0,   154,     0,    32,     0,     4,   524,     0,
       0,     0,     0,     0,     0,     0,   148,   149,  1567,     0,
       0,   150,     0,     0,    33,   151,  1017,     0,     0,     0,
       0,     5,     6,   152,     0,   153,  1129,     0,   271,   265,
     272,    21,     2,     1,     0,    69,  1475,     0,    21,     2,
       0,   271,   265,   272,    21,     2,     0,   520,     0,     0,
     872,     0,     0,     0,   873,   874,   875,     0,     0,     0,
       0,     0,     0,   876,     0,     0,  -606,     0,     0,     0,
       0,  -610,     0,   358,   273,     0,     0,   274,     0,     0,
       0,     0,     0,     0,     0,     0,     0,   273,     0,     0,
     274,   381,     0,   759,  -625,     0,     0,     0,   154,  1130,
       0,   271,   265,   272,    21,     2,     0,   271,   265,   272,
      21,     2,     0,     0,     0,   521,     0,     0,     0,    69,
     877,     0,  1036,     0,     0,     0,     0,     0,    69,  1131,
    1132,     0,    69,    69,     0,  1133,  1134,     0,   271,   265,
     272,    21,     2,   761,    67,     0,   277,   273,     0,     0,
     274,    67,     0,   273,     0,     0,   274,    67,     0,   277,
     278,   279,     0,     0,     0,     0,     0,     0,   280,   281,
       0,     0,   282,   278,   279,     0,     0,   443,     0,   523,
       0,   280,   281,     0,   273,   282,     0,   274,     0,     0,
       0,     0,     0,     0,     0,  1036,     0,     0,     0,     0,
      69,     0,     0,     0,     0,    69,     0,    69,     0,     0,
       0,     0,     0,   444,     0,     0,   445,    67,     0,   277,
       0,   446,     0,    67,     0,   277,     0,     0,     0,     0,
       0,     0,     0,   278,   279,     0,     0,     0,   767,   278,
     279,   280,   281,     0,     0,   282,     0,   280,   281,   447,
       0,   282,     0,     0,    67,   448,   449,   450,   451,   452,
     453,   454,     0,     0,     0,     0,     0,     0,     0,     0,
     661,   662,     0,     0,     0,     0,     0,    69,   663,   664,
       0,     0,   282,     0,     0,     0,     0,   455,   456,   457,
     458,   459,   460,   461,   462,   463,   464,   465,   466,   467,
     468,   469,     0,   470,   471,   472,   473,   926,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,   927,
       0,     0,     0,     0,     0,     0,     0,     0,     0,   928,
     929,   930,   927,     0,     0,     0,     0,   931,     0,   932,
       0,   933,   928,   929,   930,     0,     0,     0,     0,   934,
     931,     0,   932,     0,   933,     0,     0,     0,     0,     0,
    1340,     0,   934,     0,     0,   935,     0,   936,     0,     0,
       0,     0,     0,     0,     0,     0,   937,   938,   935,     0,
     936,   939,   940,     0,     0,   941,     0,     0,     0,   937,
     938,     0,     0,   942,   939,   940,     0,     0,   941,     0,
       0,     1,     0,     0,     0,     0,   942,     2,     0,     0,
       0,     0,   943,   -24,     0,   944,     0,     0,     0,     0,
     945,     0,     0,     0,   -24,   943,     0,     0,   944,     0,
      69,   -24,     0,   945,     0,     3,     0,     0,     0,    79,
       0,     0,     0,     0,   -24,     0,     0,     0,   -52,     0,
       0,     0,     0,     0,   -52,   -52,     0,     0,     0,   -24,
       0,     4,  -625,   -52,   -52,     0,     0,     0,   -52,     0,
       0,   -52,     0,   -52,    69,    80,     0,     0,   -24,     0,
       0,     0,     0,     0,     0,     5,     6,     0,     7,     0,
       0,     0,     0,     0,     0,     0,     0,   -52,   -52,   -52,
     -24,     0,     0,     0,     0,     0,    86,     0,   -52,   -52,
       0,     0,     0,   -52,     0,   -84,   -52,   -52,     0,     0,
      69,   -84,     0,   -52,   -52,   -52,     0,   -52,   -84,     0,
     -84,   -84,     0,     0,     0,   -84,     0,     0,   -84,    91,
     -84,     0,    87,     0,     0,     0,     0,     0,   -99,     0,
       0,     0,     0,     0,   -99,     0,     0,     0,     0,     0,
       0,     0,     0,   -99,   -84,     0,   -84,     0,   -99,     0,
       0,   -99,    94,   -99,     0,   -84,   -84,     0,     0,     0,
     -84,  -201,     0,   -84,   -84,     0,     0,  -201,  -201,     0,
     -84,   -84,   -84,     0,   -84,     0,  -201,   -99,     0,   -99,
       0,     0,     0,     0,  -201,  1089,  -201,     0,     0,   -99,
       0,     0,     0,   -99,  -812,     0,   -99,   -99,     0,     0,
    -812,     0,     0,   -99,   -99,   -99,     0,   -99,     0,  -812,
    -201,     0,  -201,     0,  -812,     0,     0,  -812,     0,  -812,
       0,     0,  -201,     0,     0,     0,  -201,     0,     0,  -201,
    -201,     0,     0,   139,     0,     0,  -201,  -201,  -201,   140,
    -201,     0,     0,  -812,     0,  -812,   142,     0,   143,   144,
       0,     0,     0,   195,     0,  -812,   145,     0,   147,  -812,
       0,     0,  -812,  -812,     0,     0,     0,     0,     0,  -812,
    -812,  -812,   139,  -812,     0,     0,     0,     0,   140,   499,
       0,     0,   196,     0,     4,     0,     0,   143,   144,     0,
       0,     0,     0,   148,   149,   145,     0,   147,   150,     0,
       0,    33,   151,     0,     0,   139,     0,     0,     5,     6,
     152,   140,   153,     0,     0,     0,     0,     0,     0,     0,
     143,    32,     0,     4,     0,   216,     0,     0,   145,     0,
     147,     0,   148,   149,     0,     0,     0,   150,   139,     0,
      33,   151,     0,     0,   140,     0,     0,     5,     6,   152,
       0,   153,     0,   143,    32,     0,     4,     0,  1212,     0,
       0,   145,     0,   147,     0,     0,   149,     0,     0,     0,
     150,   139,     0,    33,   151,     0,     0,   140,     0,     0,
       5,     6,   152,     0,   153,     0,   143,    32,     0,     4,
       0,     0,     0,     0,   145,     0,   147,     0,     0,   149,
       0,     0,     0,   150,     0,     0,    33,   151,     0,     0,
       0,     0,     0,     5,     6,   152,     0,   153,     0,     0,
      32,     0,     4,     0,     0,     0,     0,     0,     0,     0,
       0,     0,   149,     0,     0,     0,   150,     0,     0,    33,
     151,     0,     0,     0,     0,     0,     5,     6,   152,     0,
     153,   628,   629,   630,   631,   632,   633,   634,   635,   636,
     637,   638,   639,     0,     0,     0,     0,     0,     0,   640,
     641,   642,   643,   644,   645,   646,   647,   648,   649,   650,
     651,   652,   653,   654,   655,   656,   657,   658,   659,   455,
     456,   457,   458,   459,   460,   461,   462,   463,   464,   465,
     466,   467,   468,   469,     0,   470,   471,   472,   473,  -924,
    -924,  -924,  -924,  -924,  -924,  -924,  -924,  -924,  -924,  -924,
    -924,   467,   468,   469,     0,   470,   471,   472,   473
};

static const yytype_int16 yycheck[] =
{
       0,    83,    20,     3,     4,   122,    70,    92,     0,     0,
      95,    51,   105,   499,   135,    88,    31,    12,    88,    92,
     481,   476,    95,   714,   257,   124,   476,   246,    28,    29,
      30,    31,    32,    85,    34,    85,    88,   807,    88,   384,
      92,    54,    92,    95,   124,    95,   277,   278,   279,   280,
     281,    51,    88,   755,     3,   720,    92,    33,   124,    95,
     973,   973,    88,   500,    64,    65,    92,    33,   522,    95,
     522,    88,  1134,   194,   809,    92,   901,    88,    95,   997,
     815,   584,   122,     7,   976,   330,    88,   597,   661,   662,
      85,     7,     7,  1303,   694,   522,    58,    88,   984,    99,
      92,   522,    51,    95,     1,    37,    40,    28,   108,   109,
      28,   111,     1,    42,   481,  1253,    28,    42,     0,  1336,
       1,   415,   122,   244,    30,  1394,    67,   127,   143,   129,
     145,   478,   132,   148,    28,     7,   151,    12,   138,   139,
     140,    27,   142,   143,   120,   145,   146,   147,   148,   149,
     150,   151,   152,    42,   120,     7,     9,   584,   124,    28,
     621,    37,    12,   584,    36,     7,  1058,    28,     6,     7,
      53,    62,  1441,   122,  1312,    81,  1548,     7,   144,     6,
       7,    62,   182,   183,    36,    30,   186,    73,   228,    37,
     139,    37,   147,   193,    36,   195,   196,    88,   147,   320,
       1,   156,   282,  1575,   303,     6,     7,    88,   149,   185,
    1427,   558,   903,    88,  1352,   215,   216,   151,   184,   185,
     152,   515,   151,   303,  1137,  1137,  1495,   152,   228,    67,
    1440,   152,   477,  1443,   152,   159,   321,   303,    88,   154,
     152,   157,  1128,    27,   475,   476,     6,     7,   321,   543,
     147,   705,   546,   705,   621,   255,   256,   257,   152,   553,
     860,   555,   262,   152,     1,   227,   560,   153,   151,   321,
       7,   321,  1541,   846,   240,    81,   152,   522,   705,   228,
     152,   119,   248,   152,   705,   321,     6,     7,  1498,    73,
     793,   152,   119,   146,   147,   321,  1565,  1566,    27,   384,
     152,   301,     7,   813,   321,   905,   152,   273,   156,    55,
     321,   384,  1581,   313,   314,   315,   282,   312,   119,   321,
     147,    24,   322,   161,    35,   291,     6,     7,    31,   321,
     330,    40,   384,    27,   384,    15,   351,   303,    81,   584,
       6,     7,    22,    63,    73,   360,   152,  1265,   384,    58,
       0,   351,    55,   353,    59,  1517,   420,    30,   384,   119,
     360,    37,   362,   363,   330,   868,  1279,   384,     1,   153,
      36,   155,   793,     6,     7,   375,   376,  1147,    27,    73,
      46,     6,     7,   398,    60,    37,   146,   147,   619,   389,
     390,   391,   384,  1285,     6,     7,   156,    63,   398,   119,
      80,   481,    78,    83,    42,  1230,   152,   124,    81,   152,
    1472,  1573,   412,   130,   478,   118,     8,  1152,   418,   148,
     149,    63,    14,   877,    73,   877,   146,   147,     7,    42,
     661,   662,   663,   664,    21,   729,   156,     6,     7,   119,
     389,    33,   149,   523,   151,     7,    15,   151,  1113,   415,
     877,    63,   889,   119,   420,   107,   877,   912,   752,  1371,
     705,     6,     7,    62,     1,  1235,    11,   147,    30,     6,
       7,    63,    64,    37,    11,     7,   152,   517,    65,     9,
     146,   147,   148,   149,    63,   485,   119,     3,     4,     5,
       6,     7,   725,   118,   119,   147,   491,    25,    27,   148,
     149,     6,     7,    67,   146,   147,    11,   119,   146,   147,
      62,   487,    57,   606,    83,   481,   108,   517,   156,   519,
      57,   487,    52,    37,     6,     7,   625,  1229,     6,     7,
      24,    63,    44,    49,   146,   147,    52,    31,   146,   147,
      74,   621,    70,   107,    73,   625,   546,   611,   793,   515,
     119,    79,   518,   617,   152,   147,   522,   523,    30,   625,
    1473,    55,   562,   130,    78,   282,    78,   149,   517,   151,
      63,   105,   567,     7,   119,   575,   110,   543,   147,   545,
     546,    63,   119,   150,   584,   706,   303,   553,   100,   555,
     809,   671,    60,   147,   560,    53,   815,  1288,  1289,   599,
    1291,  1292,   156,   119,   604,   134,   135,   136,    12,    81,
    1502,  1503,   578,   562,   119,   846,   847,   734,   584,   135,
     136,   852,   853,   868,   118,    59,    37,   143,   144,     1,
     107,   147,   877,    16,     6,     7,     7,   119,   154,   605,
      53,   119,    13,    15,    48,   611,   879,    51,   156,    60,
     599,    34,    56,   146,   147,   621,    28,    29,    30,   625,
       7,    42,   130,   156,   146,   147,   148,   149,   107,    42,
     147,  1584,     6,     7,   130,     3,     4,     5,     6,     7,
      84,   912,     1,    30,   152,    30,    90,   156,   156,    13,
      62,   160,   705,     7,   150,    78,   147,    16,     7,    44,
      24,    35,    11,   718,    87,   671,    42,    31,   147,    81,
      24,    83,    16,  1007,    30,    34,    88,   732,   718,   436,
      44,    49,    74,   689,    52,   725,    30,    61,   146,   147,
     770,    35,   732,    78,   734,    59,  1239,   836,   156,   705,
     134,   135,   136,   709,  1514,  1090,  1201,   119,    57,   715,
     716,  1201,   104,   105,    78,   100,   836,    61,   110,    78,
      60,   146,   147,   729,   481,   482,   999,   888,    87,   147,
     770,   156,   146,   147,    78,   147,   100,  1547,     1,     6,
       7,    85,    86,    32,    11,    34,   752,   161,     1,     7,
    1084,   119,  1086,    16,     7,    13,    19,    46,  1253,   149,
    1570,    50,   152,   520,   521,    23,   523,    30,  1499,   149,
    1296,   151,    35,    31,     1,   143,   144,    66,    30,   147,
       7,   770,     1,     1,  1285,   146,   147,   793,     7,     7,
      57,   152,   949,    82,   551,   146,   147,   152,    61,   805,
      19,   156,   150,     3,     4,     5,     6,     7,   156,   919,
     161,   149,   412,   819,   152,    78,   573,  1312,   418,    32,
     148,    34,    85,    86,   152,     6,     7,   919,  1559,     4,
     836,     6,     7,   149,   147,   151,    11,    50,     1,   879,
       3,     4,     5,     6,     7,   149,  1577,   148,   152,    49,
     607,   152,    52,    66,     6,     7,   156,  1352,   158,   159,
      20,    30,   868,    15,   621,   622,  1392,    49,   625,    82,
      22,   877,   138,   139,   140,   141,    28,    29,    30,  1380,
      40,    41,    30,   923,   919,    45,    49,    39,   919,    52,
     146,   147,   149,  1152,   151,   156,   152,   148,    58,   160,
      52,   152,   149,   148,    37,   152,   912,   152,   914,   949,
     148,   149,   952,  1414,   671,    67,   146,   147,   149,   119,
       1,   152,     3,     4,     5,     6,     7,    30,    80,    81,
    1201,    83,     6,     7,    33,   975,   976,   977,    30,   138,
     139,   140,   141,  1076,   148,   149,   952,   147,  1028,    67,
       4,    42,   149,     7,  1239,   152,   119,   153,   121,   999,
     156,   148,   149,   149,   970,  1090,   152,   119,    49,   148,
     149,    52,   135,   136,    55,  1079,    57,  1090,   148,   149,
     143,   144,  1253,   149,   147,    42,   152,  1488,  1028,   147,
     149,    30,   998,   152,   157,   147,   149,   152,  1090,   152,
    1090,  1007,   148,   149,   761,  1011,  1012,  1533,   152,   149,
      27,   151,    20,    21,  1090,   772,    79,   774,  1058,   776,
    1521,   120,  1155,   149,  1090,   151,    30,    20,    21,   149,
     152,   151,  1087,  1090,   148,   149,   148,   149,   119,  1028,
     121,  1312,    47,    36,   152,   144,    17,  1087,    42,    42,
      28,    29,  1553,    46,   135,   136,    73,    42,  1090,    28,
      29,  1218,   143,   144,   151,  1399,   147,    42,    49,  1226,
     314,   315,    65,  1079,   156,   148,   148,   145,  1084,   836,
    1086,  1352,   153,   153,   148,   184,   185,   100,    30,    42,
     152,   152,   151,  1151,   149,   151,    42,    42,   855,   151,
      30,  1141,   152,   152,   152,   862,    53,   151,    19,   866,
     152,   152,   147,    67,   152,   152,   873,   134,   135,   136,
     152,   138,   139,   140,   141,   148,   148,   120,   145,   146,
     147,    79,   148,   890,    35,   156,   893,   152,   895,   156,
    1180,   240,   156,   145,   156,   152,   149,   153,  1188,   248,
    1156,   147,   152,    42,   130,   148,   149,   150,    37,   152,
     151,   149,    47,   152,    60,  1285,     1,   924,    49,  1209,
     152,    34,   152,  1295,   273,    10,    78,   147,  1218,     1,
    1220,    16,    17,    30,   152,     7,  1226,   149,    23,    17,
      25,    26,   291,  1233,    42,   156,   120,   155,    33,    34,
      35,   145,   153,    25,   961,    42,    66,    68,   965,   152,
     160,    33,   152,    35,    38,  1376,    60,   974,   150,    42,
      37,   978,   979,    30,    59,   152,    61,  1267,    47,   147,
      76,   330,    53,  1239,   991,    70,    71,    59,   150,    61,
      75,   147,   152,    78,    79,  1285,    34,  1253,    70,  1353,
      85,    86,    87,    75,    89,   153,    54,    79,   152,  1299,
    1380,   151,    67,    85,    86,   147,    49,    89,   161,   152,
     148,   148,  1327,   149,    69,  1315,  1033,  1438,   152,  1285,
       3,     4,     5,     6,     7,   152,  1043,  1327,    34,   151,
      39,    34,   150,   152,  1414,   152,   152,   152,    53,    30,
     152,  1058,   152,  1309,    30,   154,  1312,   152,    30,    72,
     152,  1068,    77,    46,    68,    30,   415,   157,   147,    67,
    1077,   420,   152,    81,   130,   152,    49,   162,    42,    52,
      36,    36,    55,    37,   152,    60,   152,    36,    30,   152,
     152,  1381,   152,   152,    42,    30,  1352,  1353,   152,     1,
     152,     3,     4,     5,     6,     7,   108,   148,   152,   152,
    1117,    34,    22,    34,    81,  1405,  1406,   152,  1488,  1126,
    1492,   152,    81,    81,  1380,     9,  1133,   149,   105,   102,
     147,     1,   105,    47,    81,    47,  1426,    36,   487,    47,
      47,  1148,  1149,  1399,   151,    30,   119,    49,   121,   152,
      52,  1521,   152,    64,    34,   152,   108,   152,  1414,    42,
    1167,   152,   135,   136,  1171,   152,   515,   105,   152,   518,
     143,   144,  1462,   522,   147,    81,    40,    30,   152,  1186,
      28,   154,   152,  1553,   152,    37,   152,    30,  1195,   152,
     150,   148,    36,    53,   543,    36,   545,   546,    19,   152,
      76,   152,   133,    30,   553,   130,   555,   152,   152,   152,
      81,   560,  1502,  1503,   150,    46,   150,   119,    42,   121,
      81,   148,   152,    60,   573,    36,    30,   153,    60,   578,
    1237,     3,  1488,   135,   136,   584,  1243,   152,    34,   152,
    1247,   143,   144,  1250,   153,   147,    81,   152,  1255,   152,
      36,    81,   152,    47,  1261,   152,   605,    10,  1548,   152,
     152,   152,   611,    16,    17,  1521,   154,   153,    30,  1276,
      23,   152,    25,    26,    81,  1282,    81,   152,  1285,   152,
      33,    34,    35,    37,   152,  1575,   152,   152,    22,    72,
      39,    81,   149,    60,   152,    47,    36,  1553,    36,    22,
    1590,     3,     4,     5,     6,     7,    59,   152,    61,    30,
      47,    36,   152,   152,    10,    18,   152,    70,    71,   152,
      16,    17,    75,   152,  1218,    78,    79,    23,    10,    25,
      26,    12,    85,    86,    87,   519,    89,    33,    34,    35,
     689,   362,  1349,    12,   195,   487,   919,    49,   770,   216,
      52,   101,   894,   126,  1361,    57,   705,  1117,   961,   852,
     709,  1368,   847,    59,   625,    61,   715,   716,  1309,    70,
    1087,    10,   273,  1380,    70,    71,   950,    16,    17,    75,
     729,   665,    78,    79,    23,  1031,    25,    26,  1506,    85,
      86,    87,  1455,    89,    33,    34,    35,  1436,   882,  1387,
    1373,  1166,  1184,   752,  1391,   970,   330,  1414,  1151,   162,
    1145,  1418,  1419,  1485,  1421,   793,   793,   119,    88,   121,
      59,  1428,    61,   952,  1107,  1228,  1226,  1408,  1424,  1287,
     491,    70,    71,   135,   136,  1091,    75,  1212,   683,    78,
      79,   143,   144,    25,   793,   147,    85,    86,    87,  1417,
      89,    33,  1478,    35,  1584,   793,   805,  1270,   599,    46,
     262,    -1,  1469,    -1,    -1,    -1,   162,    -1,    -1,    -1,
     819,    -1,    -1,    -1,    -1,    -1,    10,    59,    -1,    61,
    1487,  1488,    16,    17,    -1,    -1,    -1,    -1,    70,    23,
      -1,    25,    26,    75,    -1,    -1,  1503,    79,    -1,    33,
      34,    35,    -1,    85,    86,    -1,    -1,    89,    -1,    -1,
    1517,    -1,    -1,    -1,  1521,    -1,    -1,  1524,    -1,   868,
      -1,    -1,    -1,   162,    -1,    59,    -1,    61,   877,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    70,    71,  1545,    -1,
      -1,    75,    -1,    -1,    78,    79,  1553,    -1,    -1,    -1,
      -1,    85,    86,    87,    -1,    89,     1,    -1,     3,     4,
       5,     6,     7,     1,    -1,   914,  1573,    -1,     6,     7,
      -1,     3,     4,     5,     6,     7,    -1,    15,    -1,    -1,
      18,    -1,    -1,    -1,    22,    23,    24,    -1,    -1,    -1,
      -1,    -1,    -1,    31,    -1,    -1,    34,    -1,    -1,    -1,
      -1,    39,    -1,   952,    49,    -1,    -1,    52,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    49,    -1,    -1,
      52,   970,    -1,    55,    62,    -1,    -1,    -1,   162,    74,
      -1,     3,     4,     5,     6,     7,    -1,     3,     4,     5,
       6,     7,    -1,    -1,    -1,    83,    -1,    -1,    -1,   998,
      88,    -1,    97,    -1,    -1,    -1,    -1,    -1,  1007,   104,
     105,    -1,  1011,  1012,    -1,   110,   111,    -1,     3,     4,
       5,     6,     7,   105,   119,    -1,   121,    49,    -1,    -1,
      52,   119,    -1,    49,    -1,    -1,    52,   119,    -1,   121,
     135,   136,    -1,    -1,    -1,    -1,    -1,    -1,   143,   144,
      -1,    -1,   147,   135,   136,    -1,    -1,    12,    -1,   147,
      -1,   143,   144,    -1,    49,   147,    -1,    52,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    97,    -1,    -1,    -1,    -1,
    1079,    -1,    -1,    -1,    -1,  1084,    -1,  1086,    -1,    -1,
      -1,    -1,    -1,    48,    -1,    -1,    51,   119,    -1,   121,
      -1,    56,    -1,   119,    -1,   121,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,   135,   136,    -1,    -1,    -1,  1117,   135,
     136,   143,   144,    -1,    -1,   147,    -1,   143,   144,    84,
      -1,   147,    -1,    -1,   119,    90,    91,    92,    93,    94,
      95,    96,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
     135,   136,    -1,    -1,    -1,    -1,    -1,  1156,   143,   144,
      -1,    -1,   147,    -1,    -1,    -1,    -1,   122,   123,   124,
     125,   126,   127,   128,   129,   130,   131,   132,   133,   134,
     135,   136,    -1,   138,   139,   140,   141,     1,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    13,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    23,
      24,    25,    13,    -1,    -1,    -1,    -1,    31,    -1,    33,
      -1,    35,    23,    24,    25,    -1,    -1,    -1,    -1,    43,
      31,    -1,    33,    -1,    35,    -1,    -1,    -1,    -1,    -1,
    1239,    -1,    43,    -1,    -1,    59,    -1,    61,    -1,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    70,    71,    59,    -1,
      61,    75,    76,    -1,    -1,    79,    -1,    -1,    -1,    70,
      71,    -1,    -1,    87,    75,    76,    -1,    -1,    79,    -1,
      -1,     1,    -1,    -1,    -1,    -1,    87,     7,    -1,    -1,
      -1,    -1,   106,    13,    -1,   109,    -1,    -1,    -1,    -1,
     114,    -1,    -1,    -1,    24,   106,    -1,    -1,   109,    -1,
    1309,    31,    -1,   114,    -1,    35,    -1,    -1,    -1,     1,
      -1,    -1,    -1,    -1,    44,    -1,    -1,    -1,    10,    -1,
      -1,    -1,    -1,    -1,    16,    17,    -1,    -1,    -1,    59,
      -1,    61,    62,    25,    26,    -1,    -1,    -1,    30,    -1,
      -1,    33,    -1,    35,  1353,    37,    -1,    -1,    78,    -1,
      -1,    -1,    -1,    -1,    -1,    85,    86,    -1,    88,    -1,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    59,    60,    61,
     100,    -1,    -1,    -1,    -1,    -1,     1,    -1,    70,    71,
      -1,    -1,    -1,    75,    -1,    10,    78,    79,    -1,    -1,
    1399,    16,    -1,    85,    86,    87,    -1,    89,    23,    -1,
      25,    26,    -1,    -1,    -1,    30,    -1,    -1,    33,     1,
      35,    -1,    37,    -1,    -1,    -1,    -1,    -1,    10,    -1,
      -1,    -1,    -1,    -1,    16,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    -1,    25,    59,    -1,    61,    -1,    30,    -1,
      -1,    33,     1,    35,    -1,    70,    71,    -1,    -1,    -1,
      75,    10,    -1,    78,    79,    -1,    -1,    16,    17,    -1,
      85,    86,    87,    -1,    89,    -1,    25,    59,    -1,    61,
      -1,    -1,    -1,    -1,    33,     1,    35,    -1,    -1,    71,
      -1,    -1,    -1,    75,    10,    -1,    78,    79,    -1,    -1,
      16,    -1,    -1,    85,    86,    87,    -1,    89,    -1,    25,
      59,    -1,    61,    -1,    30,    -1,    -1,    33,    -1,    35,
      -1,    -1,    71,    -1,    -1,    -1,    75,    -1,    -1,    78,
      79,    -1,    -1,    10,    -1,    -1,    85,    86,    87,    16,
      89,    -1,    -1,    59,    -1,    61,    23,    -1,    25,    26,
      -1,    -1,    -1,    30,    -1,    71,    33,    -1,    35,    75,
      -1,    -1,    78,    79,    -1,    -1,    -1,    -1,    -1,    85,
      86,    87,    10,    89,    -1,    -1,    -1,    -1,    16,    17,
      -1,    -1,    59,    -1,    61,    -1,    -1,    25,    26,    -1,
      -1,    -1,    -1,    70,    71,    33,    -1,    35,    75,    -1,
      -1,    78,    79,    -1,    -1,    10,    -1,    -1,    85,    86,
      87,    16,    89,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      25,    59,    -1,    61,    -1,    30,    -1,    -1,    33,    -1,
      35,    -1,    70,    71,    -1,    -1,    -1,    75,    10,    -1,
      78,    79,    -1,    -1,    16,    -1,    -1,    85,    86,    87,
      -1,    89,    -1,    25,    59,    -1,    61,    -1,    30,    -1,
      -1,    33,    -1,    35,    -1,    -1,    71,    -1,    -1,    -1,
      75,    10,    -1,    78,    79,    -1,    -1,    16,    -1,    -1,
      85,    86,    87,    -1,    89,    -1,    25,    59,    -1,    61,
      -1,    -1,    -1,    -1,    33,    -1,    35,    -1,    -1,    71,
      -1,    -1,    -1,    75,    -1,    -1,    78,    79,    -1,    -1,
      -1,    -1,    -1,    85,    86,    87,    -1,    89,    -1,    -1,
      59,    -1,    61,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
      -1,    -1,    71,    -1,    -1,    -1,    75,    -1,    -1,    78,
      79,    -1,    -1,    -1,    -1,    -1,    85,    86,    87,    -1,
      89,   437,   438,   439,   440,   441,   442,   443,   444,   445,
     446,   447,   448,    -1,    -1,    -1,    -1,    -1,    -1,   455,
     456,   457,   458,   459,   460,   461,   462,   463,   464,   465,
     466,   467,   468,   469,   470,   471,   472,   473,   474,   122,
     123,   124,   125,   126,   127,   128,   129,   130,   131,   132,
     133,   134,   135,   136,    -1,   138,   139,   140,   141,   122,
     123,   124,   125,   126,   127,   128,   129,   130,   131,   132,
     133,   134,   135,   136,    -1,   138,   139,   140,   141
};

/* YYSTOS[STATE-NUM] -- The (internal number of the) accessing
   symbol of state STATE-NUM.  */
static const yytype_uint16 yystos[] =
{
       0,     1,     7,    35,    61,    85,    86,    88,   164,   165,
     166,   174,   175,   223,   225,   235,   411,   412,   416,   502,
      30,     6,   167,   502,   504,   502,     0,   174,    13,    24,
      31,    44,    59,    78,   100,   176,   177,   178,   179,   181,
     182,   191,   192,   196,   198,   202,   203,   208,   209,   518,
     523,    35,    42,   413,   151,   415,   416,   228,   226,   502,
     502,   502,   173,   502,     1,    19,   502,   119,   180,   270,
     271,   272,   273,   275,   277,   502,   504,   562,   502,     1,
      37,   189,     1,   194,     1,   200,     1,    37,   205,   207,
     560,     1,   211,   167,     1,   238,    62,   412,   152,    62,
     107,   229,   230,   232,   233,   560,   233,   234,    53,    53,
      42,   149,   152,   502,   502,    42,    25,    70,    79,   563,
     149,   152,   156,   146,   147,   252,   253,   276,   252,   276,
     147,    42,    30,   470,     1,   190,    60,   187,    30,    10,
      16,    17,    23,    25,    26,    33,    34,    35,    70,    71,
      75,    79,    87,    89,   162,   179,   195,   202,   208,   213,
     214,   216,   224,   225,   235,   289,   313,   326,   328,   331,
     335,   338,   341,   345,   346,   472,   486,   496,   497,   523,
     524,   571,    30,    16,    34,    78,    87,   179,   201,   222,
     346,   475,   496,    30,   561,    30,    59,   202,   206,   214,
     218,   225,   328,   331,   341,   345,   346,   472,   496,   497,
     523,   524,    37,   152,   559,    30,    30,   212,   213,   214,
     219,   235,   331,   345,   346,   496,   497,   227,    30,   213,
     214,   220,   235,   236,   239,   331,   345,   346,   496,   497,
     147,   420,   417,   502,   231,   230,   147,   240,    67,   559,
     502,   502,   502,    42,    42,    49,   156,   158,   159,   564,
     565,   566,   567,   570,   273,     4,    11,   167,   274,   506,
       1,     3,     5,    49,    52,    55,    57,   121,   135,   136,
     143,   144,   147,   168,   172,   257,   260,   262,   263,   269,
     270,   272,   273,   278,   279,   280,   281,   288,   316,   323,
     503,   505,   506,   147,   253,    63,   502,    63,   502,   260,
     272,    30,   519,    13,    23,    31,   502,   152,   152,   240,
     188,   185,    13,   193,   502,   336,   502,   504,   502,   361,
     362,   502,   173,    11,    57,   270,   342,   173,    11,    57,
     173,   488,   489,   502,   173,     1,   502,     1,   502,   173,
     502,    79,   152,    24,   199,   502,   502,     1,   270,   481,
     118,   502,    30,    59,   204,   502,   240,   204,    47,   152,
      59,   210,   502,   210,   233,    35,    61,   167,   237,    17,
      11,   270,   421,    42,   418,   240,     1,    25,    33,    35,
      59,    61,    70,    75,    79,    89,   223,   243,   247,   333,
     551,   552,   553,   558,   272,    42,    42,    49,   376,   502,
     502,   568,   569,   376,   156,   151,   159,   502,   569,   148,
     272,   280,   148,   269,   269,   269,   269,   269,   260,   284,
     285,   288,   323,   254,    27,    73,   325,    12,    48,    51,
      56,    84,    90,    12,    48,    51,    56,    84,    90,    91,
      92,    93,    94,    95,    96,   122,   123,   124,   125,   126,
     127,   128,   129,   130,   131,   132,   133,   134,   135,   136,
     138,   139,   140,   141,   261,   145,    63,   146,   272,   318,
     148,   149,   153,   502,   148,   100,   520,   100,   178,   179,
     517,   521,   522,   183,   502,   183,   183,   152,   240,    17,
     184,   186,   213,   214,   215,   235,   328,   331,   341,   345,
     346,   496,   497,   502,   152,   151,   337,    53,   151,    30,
      15,    83,    88,   147,   270,   278,   363,   364,   365,   380,
     384,   385,   386,   387,   395,   399,   405,   411,   429,   459,
     502,    42,   471,   151,   343,   151,   151,    37,    78,   487,
     492,   151,    42,   151,   152,    42,   152,     1,    42,   290,
     151,    42,   151,   173,   502,   152,    30,   479,   173,   199,
     502,   152,   152,   147,   249,    19,   152,   152,    67,     6,
     502,   502,   152,   424,   425,   148,   422,   148,   220,   221,
     414,   419,   148,   502,   504,   554,   502,   502,    79,    35,
     241,   173,   502,   152,   156,   160,   525,   147,   156,   502,
     568,   272,   314,   316,   156,   502,   252,   272,   282,   145,
     148,   149,   153,   286,   148,   149,   255,   260,   263,   263,
     263,   263,   263,   263,   263,   263,   263,   263,   263,   263,
     263,   263,   263,   263,   263,   263,   263,   263,   263,   263,
     263,   263,   263,   263,   263,   263,   263,   263,   263,   263,
     269,   135,   136,   143,   144,   264,   266,   268,   269,   277,
     324,   147,   278,   252,   317,   318,   260,   284,   285,   260,
     502,   152,   180,    30,   522,   152,   361,   470,   314,    42,
      11,    57,   167,   347,   348,   272,   193,   260,   260,   387,
     399,   429,   459,   152,   130,   151,    37,   474,   314,   149,
     344,   272,   314,   502,    47,    24,    31,    55,   118,   490,
     493,   152,    60,   491,   260,    49,   314,   314,   152,     8,
      14,    33,    64,   108,   147,   291,   292,   295,   301,   305,
     307,   311,   312,   318,   507,   512,   152,   314,   147,   501,
     502,   504,   151,    34,   179,   477,   480,   152,   152,    55,
     102,   105,   154,   256,   258,   259,   260,   270,   502,   272,
      30,    22,    39,    52,    67,    80,    83,   395,   426,   427,
     428,   429,   434,   441,   448,   451,   452,   455,   458,   459,
     460,   462,   463,   464,   465,   466,   502,   533,   534,   536,
     537,   538,   542,   545,   547,   149,   423,    17,   107,   147,
     556,    42,   556,   554,   148,   152,   242,   151,   502,    67,
     161,   272,   526,   527,   152,   559,   260,   156,   252,   315,
     120,   252,   283,   269,   284,   260,   155,   287,   257,   266,
     266,   269,   269,   134,   135,   136,   265,   325,   138,   139,
     140,   141,   267,   145,   260,   153,   152,   520,   152,     1,
     270,   349,    42,   525,   152,   152,    66,   431,    68,    38,
     396,   398,    18,    22,    23,    24,    31,    88,   270,   379,
     387,   399,   406,   408,   429,   459,   529,   240,    60,   473,
     150,   327,   270,     9,    42,    55,   339,   249,   272,   270,
     173,   492,    30,    47,     1,   376,    21,    65,   330,   334,
     152,   314,   147,   319,    53,   173,   310,     1,    19,   508,
     169,   502,   506,    76,   150,   332,     1,    13,    23,    24,
      25,    31,    33,    35,    43,    59,    61,    70,    71,    75,
      76,    79,    87,   106,   109,   114,   351,   498,   500,   147,
     314,   152,    34,   475,   476,   478,   482,   483,   197,   260,
     250,   153,   237,     1,   157,   432,   260,   152,   260,   461,
      54,   469,   260,   130,   150,    32,    34,    50,    66,    82,
     358,   359,   360,   450,    22,   270,   395,   429,   462,   466,
     547,    81,   152,   151,   270,   424,   557,   243,    67,    49,
     556,   243,    20,    40,    41,    45,    58,   246,   248,   272,
     161,    67,   149,   528,   152,   148,   148,   260,   288,   266,
     264,   268,   269,   148,   260,   152,   152,   525,   149,   350,
     260,   151,   260,    69,   430,   395,    97,   260,   388,   389,
     392,    74,   105,   110,   397,   366,     1,   260,   387,   399,
     429,   459,   381,   382,   376,   377,   357,   359,    39,   352,
     353,   152,   240,   470,   260,   152,   260,   248,   340,   260,
     152,   152,   491,    34,   249,   152,   525,   150,   329,   272,
     304,   314,   322,   324,    53,   272,   151,   308,    30,     1,
     513,    30,   179,   224,   346,   509,   511,   524,   293,   299,
     502,   260,   152,   152,   154,   499,   148,   149,   494,   495,
     502,   506,   332,   488,   502,    30,   148,   149,   251,   259,
     152,    30,   260,    72,   152,   421,    77,   468,    68,     1,
      74,   104,   105,   110,   111,   389,   392,   544,   260,   454,
     502,   464,   502,   457,   502,   260,   260,    46,   432,   150,
     260,    30,   147,   241,   272,   376,    67,   314,   272,   272,
     152,   167,   152,   351,   260,   152,   130,     9,    52,   393,
     152,    81,   390,   260,    42,   375,    36,    36,    37,    60,
     147,   378,    37,    60,    36,   260,   464,   502,    30,   407,
     410,   152,   152,   152,   260,    42,   152,   152,   559,   260,
     152,    63,   302,   320,   314,   314,    30,   309,   310,   108,
     510,    30,    30,   220,   514,   516,   510,   148,   149,   294,
     297,   152,   148,   152,   498,   148,   149,   152,    78,   485,
     492,    34,   256,    22,   435,   442,   260,    34,   467,   432,
     152,    40,    58,   543,   260,   543,   152,    81,   389,   392,
      81,   453,   502,    40,   151,    81,   456,   430,   424,   260,
     260,    28,   152,   546,   415,   243,   148,    42,   555,   559,
     272,    20,    21,   245,   396,   260,     9,   394,   260,   149,
     391,   105,   147,   374,     1,    81,   530,   531,    47,    47,
     502,    47,    47,    17,   216,   217,   403,    36,   260,    36,
     152,   409,   502,    30,   152,   260,   152,   154,   148,   149,
     303,   148,   149,   321,   152,    64,   502,   108,   515,   515,
     169,    30,   298,   300,   502,   152,   494,   118,   490,   475,
     484,   491,   152,   157,   433,   502,    42,   424,   260,   152,
     270,   278,   395,   550,   260,   105,   152,   260,   152,    81,
     260,   152,    40,   272,   322,   260,   152,   152,    30,   436,
     260,    28,   152,   152,   241,   154,   502,   555,   150,   244,
     400,   401,   260,    28,   392,   260,    37,   372,    30,   285,
     464,    30,   530,   532,   249,   249,   148,   249,   249,    17,
     216,   361,   403,   404,    36,   502,   152,   152,   409,    53,
     272,   304,   322,   306,   502,    19,    76,   296,   133,   173,
     152,    30,   152,   152,    81,   439,   445,   130,   150,    81,
     152,    28,   152,   535,   260,   322,    46,    42,    81,   260,
     148,   260,   152,   389,   402,   388,   148,   240,    60,   370,
      36,   153,   285,    36,   152,   502,    30,    60,   383,   152,
     383,   152,   407,   361,   355,   404,   152,   314,    63,   502,
     502,   170,   171,   505,   152,    34,   285,   437,    28,    29,
     443,   446,   104,   539,   544,   260,   548,   549,   260,   260,
     152,   152,   535,   449,   502,   439,   260,    81,    81,   375,
     152,   240,   368,   409,   404,   153,   409,   152,    36,    47,
     152,   152,    28,    29,   354,   356,   355,   152,   502,   152,
     153,    30,   438,   439,   444,   260,    30,   543,   392,   540,
     541,    81,   152,   546,    81,   152,   152,   437,   260,   285,
      37,   373,   152,    17,   216,   369,   152,   404,   152,   409,
     249,    36,   464,   502,   260,   464,   354,   440,    22,   424,
      72,    39,   548,    81,   285,   152,   260,    30,   149,    47,
      60,   371,   361,   152,   404,    36,    36,   260,   424,   433,
     447,   152,   285,   149,   152,    22,   249,    47,    30,   404,
     404,    36,   152,   424,   149,   152,   548,   433,   152,   249,
      18,   404,   540,   152,   152,   367,   502,   152
};

#define yyerrok		(yyerrstatus = 0)
#define yyclearin	(yychar = YYEMPTY)
#define YYEMPTY		(-2)
#define YYEOF		0

#define YYACCEPT	goto yyacceptlab
#define YYABORT		goto yyabortlab
#define YYERROR		goto yyerrorlab


/* Like YYERROR except do call yyerror.  This remains here temporarily
   to ease the transition to the new meaning of YYERROR, for GCC.
   Once GCC version 2 has supplanted version 1, this can go.  */

#define YYFAIL		goto yyerrlab

#define YYRECOVERING()  (!!yyerrstatus)

#define YYBACKUP(Token, Value)					\
do								\
  if (yychar == YYEMPTY && yylen == 1)				\
    {								\
      yychar = (Token);						\
      yylval = (Value);						\
      yytoken = YYTRANSLATE (yychar);				\
      YYPOPSTACK (1);						\
      goto yybackup;						\
    }								\
  else								\
    {								\
      yyerror (YY_("syntax error: cannot back up")); \
      YYERROR;							\
    }								\
while (YYID (0))


#define YYTERROR	1
#define YYERRCODE	256


/* YYLLOC_DEFAULT -- Set CURRENT to span from RHS[1] to RHS[N].
   If N is 0, then set CURRENT to the empty location which ends
   the previous symbol: RHS[0] (always defined).  */

#define YYRHSLOC(Rhs, K) ((Rhs)[K])
#ifndef YYLLOC_DEFAULT
# define YYLLOC_DEFAULT(Current, Rhs, N)				\
    do									\
      if (YYID (N))                                                    \
	{								\
	  (Current).first_line   = YYRHSLOC (Rhs, 1).first_line;	\
	  (Current).first_column = YYRHSLOC (Rhs, 1).first_column;	\
	  (Current).last_line    = YYRHSLOC (Rhs, N).last_line;		\
	  (Current).last_column  = YYRHSLOC (Rhs, N).last_column;	\
	}								\
      else								\
	{								\
	  (Current).first_line   = (Current).last_line   =		\
	    YYRHSLOC (Rhs, 0).last_line;				\
	  (Current).first_column = (Current).last_column =		\
	    YYRHSLOC (Rhs, 0).last_column;				\
	}								\
    while (YYID (0))
#endif


/* YY_LOCATION_PRINT -- Print the location on the stream.
   This macro was not mandated originally: define only if we know
   we won't break user code: when these are the locations we know.  */

#ifndef YY_LOCATION_PRINT
# if defined YYLTYPE_IS_TRIVIAL && YYLTYPE_IS_TRIVIAL
#  define YY_LOCATION_PRINT(File, Loc)			\
     fprintf (File, "%d.%d-%d.%d",			\
	      (Loc).first_line, (Loc).first_column,	\
	      (Loc).last_line,  (Loc).last_column)
# else
#  define YY_LOCATION_PRINT(File, Loc) ((void) 0)
# endif
#endif


/* YYLEX -- calling `yylex' with the right arguments.  */

#ifdef YYLEX_PARAM
# define YYLEX yylex (YYLEX_PARAM)
#else
# define YYLEX yylex ()
#endif

/* Enable debugging if requested.  */
#if YYDEBUG

# ifndef YYFPRINTF
#  include <stdio.h> /* INFRINGES ON USER NAME SPACE */
#  define YYFPRINTF fprintf
# endif

# define YYDPRINTF(Args)			\
do {						\
  if (yydebug)					\
    YYFPRINTF Args;				\
} while (YYID (0))

# define YY_SYMBOL_PRINT(Title, Type, Value, Location)			  \
do {									  \
  if (yydebug)								  \
    {									  \
      YYFPRINTF (stderr, "%s ", Title);					  \
      yy_symbol_print (stderr,						  \
		  Type, Value); \
      YYFPRINTF (stderr, "\n");						  \
    }									  \
} while (YYID (0))


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

/*ARGSUSED*/
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_symbol_value_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
#else
static void
yy_symbol_value_print (yyoutput, yytype, yyvaluep)
    FILE *yyoutput;
    int yytype;
    YYSTYPE const * const yyvaluep;
#endif
{
  if (!yyvaluep)
    return;
# ifdef YYPRINT
  if (yytype < YYNTOKENS)
    YYPRINT (yyoutput, yytoknum[yytype], *yyvaluep);
# else
  YYUSE (yyoutput);
# endif
  switch (yytype)
    {
      default:
	break;
    }
}


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_symbol_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
#else
static void
yy_symbol_print (yyoutput, yytype, yyvaluep)
    FILE *yyoutput;
    int yytype;
    YYSTYPE const * const yyvaluep;
#endif
{
  if (yytype < YYNTOKENS)
    YYFPRINTF (yyoutput, "token %s (", yytname[yytype]);
  else
    YYFPRINTF (yyoutput, "nterm %s (", yytname[yytype]);

  yy_symbol_value_print (yyoutput, yytype, yyvaluep);
  YYFPRINTF (yyoutput, ")");
}

/*------------------------------------------------------------------.
| yy_stack_print -- Print the state stack from its BOTTOM up to its |
| TOP (included).                                                   |
`------------------------------------------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_stack_print (yytype_int16 *bottom, yytype_int16 *top)
#else
static void
yy_stack_print (bottom, top)
    yytype_int16 *bottom;
    yytype_int16 *top;
#endif
{
  YYFPRINTF (stderr, "Stack now");
  for (; bottom <= top; ++bottom)
    YYFPRINTF (stderr, " %d", *bottom);
  YYFPRINTF (stderr, "\n");
}

# define YY_STACK_PRINT(Bottom, Top)				\
do {								\
  if (yydebug)							\
    yy_stack_print ((Bottom), (Top));				\
} while (YYID (0))


/*------------------------------------------------.
| Report that the YYRULE is going to be reduced.  |
`------------------------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_reduce_print (YYSTYPE *yyvsp, int yyrule)
#else
static void
yy_reduce_print (yyvsp, yyrule)
    YYSTYPE *yyvsp;
    int yyrule;
#endif
{
  int yynrhs = yyr2[yyrule];
  int yyi;
  unsigned long int yylno = yyrline[yyrule];
  YYFPRINTF (stderr, "Reducing stack by rule %d (line %lu):\n",
	     yyrule - 1, yylno);
  /* The symbols being reduced.  */
  for (yyi = 0; yyi < yynrhs; yyi++)
    {
      fprintf (stderr, "   $%d = ", yyi + 1);
      yy_symbol_print (stderr, yyrhs[yyprhs[yyrule] + yyi],
		       &(yyvsp[(yyi + 1) - (yynrhs)])
		       		       );
      fprintf (stderr, "\n");
    }
}

# define YY_REDUCE_PRINT(Rule)		\
do {					\
  if (yydebug)				\
    yy_reduce_print (yyvsp, Rule); \
} while (YYID (0))

/* Nonzero means print parse trace.  It is left uninitialized so that
   multiple parsers can coexist.  */
int yydebug;
#else /* !YYDEBUG */
# define YYDPRINTF(Args)
# define YY_SYMBOL_PRINT(Title, Type, Value, Location)
# define YY_STACK_PRINT(Bottom, Top)
# define YY_REDUCE_PRINT(Rule)
#endif /* !YYDEBUG */


/* YYINITDEPTH -- initial size of the parser's stacks.  */
#ifndef	YYINITDEPTH
# define YYINITDEPTH 200
#endif

/* YYMAXDEPTH -- maximum size the stacks can grow to (effective only
   if the built-in stack extension method is used).

   Do not make this value too large; the results are undefined if
   YYSTACK_ALLOC_MAXIMUM < YYSTACK_BYTES (YYMAXDEPTH)
   evaluated with infinite-precision integer arithmetic.  */

#ifndef YYMAXDEPTH
# define YYMAXDEPTH 10000
#endif



#if YYERROR_VERBOSE

# ifndef yystrlen
#  if defined __GLIBC__ && defined _STRING_H
#   define yystrlen strlen
#  else
/* Return the length of YYSTR.  */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static YYSIZE_T
yystrlen (const char *yystr)
#else
static YYSIZE_T
yystrlen (yystr)
    const char *yystr;
#endif
{
  YYSIZE_T yylen;
  for (yylen = 0; yystr[yylen]; yylen++)
    continue;
  return yylen;
}
#  endif
# endif

# ifndef yystpcpy
#  if defined __GLIBC__ && defined _STRING_H && defined _GNU_SOURCE
#   define yystpcpy stpcpy
#  else
/* Copy YYSRC to YYDEST, returning the address of the terminating '\0' in
   YYDEST.  */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static char *
yystpcpy (char *yydest, const char *yysrc)
#else
static char *
yystpcpy (yydest, yysrc)
    char *yydest;
    const char *yysrc;
#endif
{
  char *yyd = yydest;
  const char *yys = yysrc;

  while ((*yyd++ = *yys++) != '\0')
    continue;

  return yyd - 1;
}
#  endif
# endif

# ifndef yytnamerr
/* Copy to YYRES the contents of YYSTR after stripping away unnecessary
   quotes and backslashes, so that it's suitable for yyerror.  The
   heuristic is that double-quoting is unnecessary unless the string
   contains an apostrophe, a comma, or backslash (other than
   backslash-backslash).  YYSTR is taken from yytname.  If YYRES is
   null, do not copy; instead, return the length of what the result
   would have been.  */
static YYSIZE_T
yytnamerr (char *yyres, const char *yystr)
{
  if (*yystr == '"')
    {
      YYSIZE_T yyn = 0;
      char const *yyp = yystr;

      for (;;)
	switch (*++yyp)
	  {
	  case '\'':
	  case ',':
	    goto do_not_strip_quotes;

	  case '\\':
	    if (*++yyp != '\\')
	      goto do_not_strip_quotes;
	    /* Fall through.  */
	  default:
	    if (yyres)
	      yyres[yyn] = *yyp;
	    yyn++;
	    break;

	  case '"':
	    if (yyres)
	      yyres[yyn] = '\0';
	    return yyn;
	  }
    do_not_strip_quotes: ;
    }

  if (! yyres)
    return yystrlen (yystr);

  return yystpcpy (yyres, yystr) - yyres;
}
# endif

/* Copy into YYRESULT an error message about the unexpected token
   YYCHAR while in state YYSTATE.  Return the number of bytes copied,
   including the terminating null byte.  If YYRESULT is null, do not
   copy anything; just return the number of bytes that would be
   copied.  As a special case, return 0 if an ordinary "syntax error"
   message will do.  Return YYSIZE_MAXIMUM if overflow occurs during
   size calculation.  */
static YYSIZE_T
yysyntax_error (char *yyresult, int yystate, int yychar)
{
  int yyn = yypact[yystate];

  if (! (YYPACT_NINF < yyn && yyn <= YYLAST))
    return 0;
  else
    {
      int yytype = YYTRANSLATE (yychar);
      YYSIZE_T yysize0 = yytnamerr (0, yytname[yytype]);
      YYSIZE_T yysize = yysize0;
      YYSIZE_T yysize1;
      int yysize_overflow = 0;
      enum { YYERROR_VERBOSE_ARGS_MAXIMUM = 5 };
      char const *yyarg[YYERROR_VERBOSE_ARGS_MAXIMUM];
      int yyx;

# if 0
      /* This is so xgettext sees the translatable formats that are
	 constructed on the fly.  */
      YY_("syntax error, unexpected %s");
      YY_("syntax error, unexpected %s, expecting %s");
      YY_("syntax error, unexpected %s, expecting %s or %s");
      YY_("syntax error, unexpected %s, expecting %s or %s or %s");
      YY_("syntax error, unexpected %s, expecting %s or %s or %s or %s");
# endif
      char *yyfmt;
      char const *yyf;
      static char const yyunexpected[] = "syntax error, unexpected %s";
      static char const yyexpecting[] = ", expecting %s";
      static char const yyor[] = " or %s";
      char yyformat[sizeof yyunexpected
		    + sizeof yyexpecting - 1
		    + ((YYERROR_VERBOSE_ARGS_MAXIMUM - 2)
		       * (sizeof yyor - 1))];
      char const *yyprefix = yyexpecting;

      /* Start YYX at -YYN if negative to avoid negative indexes in
	 YYCHECK.  */
      int yyxbegin = yyn < 0 ? -yyn : 0;

      /* Stay within bounds of both yycheck and yytname.  */
      int yychecklim = YYLAST - yyn + 1;
      int yyxend = yychecklim < YYNTOKENS ? yychecklim : YYNTOKENS;
      int yycount = 1;

      yyarg[0] = yytname[yytype];
      yyfmt = yystpcpy (yyformat, yyunexpected);

      for (yyx = yyxbegin; yyx < yyxend; ++yyx)
	if (yycheck[yyx + yyn] == yyx && yyx != YYTERROR)
	  {
	    if (yycount == YYERROR_VERBOSE_ARGS_MAXIMUM)
	      {
		yycount = 1;
		yysize = yysize0;
		yyformat[sizeof yyunexpected - 1] = '\0';
		break;
	      }
	    yyarg[yycount++] = yytname[yyx];
	    yysize1 = yysize + yytnamerr (0, yytname[yyx]);
	    yysize_overflow |= (yysize1 < yysize);
	    yysize = yysize1;
	    yyfmt = yystpcpy (yyfmt, yyprefix);
	    yyprefix = yyor;
	  }

      yyf = YY_(yyformat);
      yysize1 = yysize + yystrlen (yyf);
      yysize_overflow |= (yysize1 < yysize);
      yysize = yysize1;

      if (yysize_overflow)
	return YYSIZE_MAXIMUM;

      if (yyresult)
	{
	  /* Avoid sprintf, as that infringes on the user's name space.
	     Don't have undefined behavior even if the translation
	     produced a string with the wrong number of "%s"s.  */
	  char *yyp = yyresult;
	  int yyi = 0;
	  while ((*yyp = *yyf) != '\0')
	    {
	      if (*yyp == '%' && yyf[1] == 's' && yyi < yycount)
		{
		  yyp += yytnamerr (yyp, yyarg[yyi++]);
		  yyf += 2;
		}
	      else
		{
		  yyp++;
		  yyf++;
		}
	    }
	}
      return yysize;
    }
}
#endif /* YYERROR_VERBOSE */


/*-----------------------------------------------.
| Release the memory associated to this symbol.  |
`-----------------------------------------------*/

/*ARGSUSED*/
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yydestruct (const char *yymsg, int yytype, YYSTYPE *yyvaluep)
#else
static void
yydestruct (yymsg, yytype, yyvaluep)
    const char *yymsg;
    int yytype;
    YYSTYPE *yyvaluep;
#endif
{
  YYUSE (yyvaluep);

  if (!yymsg)
    yymsg = "Deleting";
  YY_SYMBOL_PRINT (yymsg, yytype, yyvaluep, yylocationp);

  switch (yytype)
    {

      default:
	break;
    }
}


/* Prevent warnings from -Wmissing-prototypes.  */

#ifdef YYPARSE_PARAM
#if defined __STDC__ || defined __cplusplus
int yyparse (void *YYPARSE_PARAM);
#else
int yyparse ();
#endif
#else /* ! YYPARSE_PARAM */
#if defined __STDC__ || defined __cplusplus
int yyparse (void);
#else
int yyparse ();
#endif
#endif /* ! YYPARSE_PARAM */



/* The look-ahead symbol.  */
int yychar;

/* The semantic value of the look-ahead symbol.  */
YYSTYPE yylval;

/* Number of syntax errors so far.  */
int yynerrs;



/*----------.
| yyparse.  |
`----------*/

#ifdef YYPARSE_PARAM
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
int
yyparse (void *YYPARSE_PARAM)
#else
int
yyparse (YYPARSE_PARAM)
    void *YYPARSE_PARAM;
#endif
#else /* ! YYPARSE_PARAM */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
int
yyparse (void)
#else
int
yyparse ()

#endif
#endif
{
  
  int yystate;
  int yyn;
  int yyresult;
  /* Number of tokens to shift before error messages enabled.  */
  int yyerrstatus;
  /* Look-ahead token as an internal (translated) token number.  */
  int yytoken = 0;
#if YYERROR_VERBOSE
  /* Buffer for error messages, and its allocated size.  */
  char yymsgbuf[128];
  char *yymsg = yymsgbuf;
  YYSIZE_T yymsg_alloc = sizeof yymsgbuf;
#endif

  /* Three stacks and their tools:
     `yyss': related to states,
     `yyvs': related to semantic values,
     `yyls': related to locations.

     Refer to the stacks thru separate pointers, to allow yyoverflow
     to reallocate them elsewhere.  */

  /* The state stack.  */
  yytype_int16 yyssa[YYINITDEPTH];
  yytype_int16 *yyss = yyssa;
  yytype_int16 *yyssp;

  /* The semantic value stack.  */
  YYSTYPE yyvsa[YYINITDEPTH];
  YYSTYPE *yyvs = yyvsa;
  YYSTYPE *yyvsp;



#define YYPOPSTACK(N)   (yyvsp -= (N), yyssp -= (N))

  YYSIZE_T yystacksize = YYINITDEPTH;

  /* The variables used to return semantic value and location from the
     action routines.  */
  YYSTYPE yyval;


  /* The number of symbols on the RHS of the reduced rule.
     Keep to zero when no symbol should be popped.  */
  int yylen = 0;

  YYDPRINTF ((stderr, "Starting parse\n"));

  yystate = 0;
  yyerrstatus = 0;
  yynerrs = 0;
  yychar = YYEMPTY;		/* Cause a token to be read.  */

  /* Initialize stack pointers.
     Waste one element of value and location stack
     so that they stay on the same level as the state stack.
     The wasted elements are never initialized.  */

  yyssp = yyss;
  yyvsp = yyvs;


  /* User initialization code.  */

{ yydebug=0; }
/* Line 1078 of yacc.c.  */

  goto yysetstate;

/*------------------------------------------------------------.
| yynewstate -- Push a new state, which is found in yystate.  |
`------------------------------------------------------------*/
 yynewstate:
  /* In all cases, when you get here, the value and location stacks
     have just been pushed.  So pushing a state here evens the stacks.  */
  yyssp++;

 yysetstate:
  *yyssp = yystate;

  if (yyss + yystacksize - 1 <= yyssp)
    {
      /* Get the current used size of the three stacks, in elements.  */
      YYSIZE_T yysize = yyssp - yyss + 1;

#ifdef yyoverflow
      {
	/* Give user a chance to reallocate the stack.  Use copies of
	   these so that the &'s don't force the real ones into
	   memory.  */
	YYSTYPE *yyvs1 = yyvs;
	yytype_int16 *yyss1 = yyss;


	/* Each stack pointer address is followed by the size of the
	   data in use in that stack, in bytes.  This used to be a
	   conditional around just the two extra args, but that might
	   be undefined if yyoverflow is a macro.  */
	yyoverflow (YY_("memory exhausted"),
		    &yyss1, yysize * sizeof (*yyssp),
		    &yyvs1, yysize * sizeof (*yyvsp),

		    &yystacksize);

	yyss = yyss1;
	yyvs = yyvs1;
      }
#else /* no yyoverflow */
# ifndef YYSTACK_RELOCATE
      goto yyexhaustedlab;
# else
      /* Extend the stack our own way.  */
      if (YYMAXDEPTH <= yystacksize)
	goto yyexhaustedlab;
      yystacksize *= 2;
      if (YYMAXDEPTH < yystacksize)
	yystacksize = YYMAXDEPTH;

      {
	yytype_int16 *yyss1 = yyss;
	union yyalloc *yyptr =
	  (union yyalloc *) YYSTACK_ALLOC (YYSTACK_BYTES (yystacksize));
	if (! yyptr)
	  goto yyexhaustedlab;
	YYSTACK_RELOCATE (yyss);
	YYSTACK_RELOCATE (yyvs);

#  undef YYSTACK_RELOCATE
	if (yyss1 != yyssa)
	  YYSTACK_FREE (yyss1);
      }
# endif
#endif /* no yyoverflow */

      yyssp = yyss + yysize - 1;
      yyvsp = yyvs + yysize - 1;


      YYDPRINTF ((stderr, "Stack size increased to %lu\n",
		  (unsigned long int) yystacksize));

      if (yyss + yystacksize - 1 <= yyssp)
	YYABORT;
    }

  YYDPRINTF ((stderr, "Entering state %d\n", yystate));

  goto yybackup;

/*-----------.
| yybackup.  |
`-----------*/
yybackup:

  /* Do appropriate processing given the current state.  Read a
     look-ahead token if we need one and don't already have one.  */

  /* First try to decide what to do without reference to look-ahead token.  */
  yyn = yypact[yystate];
  if (yyn == YYPACT_NINF)
    goto yydefault;

  /* Not known => get a look-ahead token if don't already have one.  */

  /* YYCHAR is either YYEMPTY or YYEOF or a valid look-ahead symbol.  */
  if (yychar == YYEMPTY)
    {
      YYDPRINTF ((stderr, "Reading a token: "));
      yychar = YYLEX;
    }

  if (yychar <= YYEOF)
    {
      yychar = yytoken = YYEOF;
      YYDPRINTF ((stderr, "Now at end of input.\n"));
    }
  else
    {
      yytoken = YYTRANSLATE (yychar);
      YY_SYMBOL_PRINT ("Next token is", yytoken, &yylval, &yylloc);
    }

  /* If the proper action on seeing token YYTOKEN is to reduce or to
     detect an error, take that action.  */
  yyn += yytoken;
  if (yyn < 0 || YYLAST < yyn || yycheck[yyn] != yytoken)
    goto yydefault;
  yyn = yytable[yyn];
  if (yyn <= 0)
    {
      if (yyn == 0 || yyn == YYTABLE_NINF)
	goto yyerrlab;
      yyn = -yyn;
      goto yyreduce;
    }

  if (yyn == YYFINAL)
    YYACCEPT;

  /* Count tokens shifted since error; after three, turn off error
     status.  */
  if (yyerrstatus)
    yyerrstatus--;

  /* Shift the look-ahead token.  */
  YY_SYMBOL_PRINT ("Shifting", yytoken, &yylval, &yylloc);

  /* Discard the shifted token unless it is eof.  */
  if (yychar != YYEOF)
    yychar = YYEMPTY;

  yystate = yyn;
  *++yyvsp = yylval;

  goto yynewstate;


/*-----------------------------------------------------------.
| yydefault -- do the default action for the current state.  |
`-----------------------------------------------------------*/
yydefault:
  yyn = yydefact[yystate];
  if (yyn == 0)
    goto yyerrlab;
  goto yyreduce;


/*-----------------------------.
| yyreduce -- Do a reduction.  |
`-----------------------------*/
yyreduce:
  /* yyn is the number of a rule to reduce with.  */
  yylen = yyr2[yyn];

  /* If YYLEN is nonzero, implement the default value of the action:
     `$$ = $1'.

     Otherwise, the following line sets YYVAL to garbage.
     This behavior is undocumented and Bison
     users should not rely upon it.  Assigning to YYVAL
     unconditionally makes the parser a bit smaller, and it avoids a
     GCC warning that YYVAL may be used uninitialized.  */
  yyval = yyvsp[1-yylen];


  YY_REDUCE_PRINT (yyn);
  switch (yyn)
    {
        case 8:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 9:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 10:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 11:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 12:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 13:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 14:

    { (yyval.qstr)="null"; ;}
    break;

  case 15:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 16:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 17:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 18:

    { (yyval.qstr)=""; ;}
    break;

  case 19:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 20:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 21:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 22:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+","+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 35:

    {
                   if ( parse_sec==0 && Config_getBool("SHOW_INCLUDE_FILES") )
                   {
                     addVhdlType((yyvsp[(2) - (3)].qstr),getParsedLine(t_LIBRARY),Entry::VARIABLE_SEC,VhdlDocGen::LIBRARY,(yyvsp[(2) - (3)].qstr).data(),"_library_");
                   }
                   (yyval.qstr)="library "+(yyvsp[(2) - (3)].qstr);
                 ;}
    break;

  case 36:

    {
                   QStringList ql1=QStringList::split(",",(yyvsp[(2) - (3)].qstr),FALSE);
                   for (uint j=0;j<ql1.count();j++)
                   {
                     //QStringList ql=QStringList::split(".",ql1[j],FALSE);
                     QCString it=ql1[j].utf8();
                     if ( parse_sec==0 && Config_getBool("SHOW_INCLUDE_FILES") )
                     {
                       addVhdlType(it,getParsedLine(t_USE),Entry::VARIABLE_SEC,VhdlDocGen::USE,it.data(),"_use_");
                     }
                   }
                   (yyval.qstr)="use "+(yyvsp[(2) - (3)].qstr);
                 ;}
    break;

  case 37:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 38:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+","+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 41:

    {
                (yyval.qstr)=(yyvsp[(2) - (3)].qstr);
                lastEntity=current;
                lastCompound=0;
                 getParsedLine(t_ENTITY);
                addVhdlType((yyval.qstr),getParsedLine(t_ENTITY),Entry::CLASS_SEC,VhdlDocGen::ENTITY,0,0,Public);
               ;}
    break;

  case 49:

    { (yyval.qstr)=""; ;}
    break;

  case 50:

    { currP=VhdlDocGen::PORT; ;}
    break;

  case 51:

    { currP=0; ;}
    break;

  case 52:

    { (yyval.qstr)=""; ;}
    break;

  case 53:

    { currP=VhdlDocGen::GENERIC;parse_sec=GEN_SEC; ;}
    break;

  case 54:

    { currP=0;parse_sec=0; ;}
    break;

  case 55:

    { currP=0; ;}
    break;

  case 56:

    {lastCompound=0;;}
    break;

  case 57:

    {lastCompound=0;;}
    break;

  case 58:

    {
                  (yyval.qstr)=(yyvsp[(4) - (5)].qstr)+"::"+(yyvsp[(2) - (5)].qstr);
                  genLabels.resize(0);
                  pushLabel(genLabels,(yyvsp[(2) - (5)].qstr));
                  lastCompound=current;
                  addVhdlType((yyval.qstr),getParsedLine(t_ARCHITECTURE),Entry::CLASS_SEC,VhdlDocGen::ARCHITECTURE,0,0,Private);
                ;}
    break;

  case 63:

    { (yyval.qstr)=""; ;}
    break;

  case 66:

    { genLabels.resize(0); ;}
    break;

  case 67:

    { genLabels.resize(0); ;}
    break;

  case 68:

    {
                  QCString k=(yyvsp[(3) - (7)].qstr);
                  QCString k2=(yyvsp[(2) - (7)].qstr);        
                  confName="";
                ;}
    break;

  case 69:

    {
                  forL.resize(0);
                  confName=(yyvsp[(2) - (5)].qstr)+"::"+(yyvsp[(4) - (5)].qstr);
                  addVhdlType((yyvsp[(2) - (5)].qstr).data(),getParsedLine(t_CONFIGURATION),Entry::VARIABLE_SEC,VhdlDocGen::CONFIG,"configuration",(yyvsp[(4) - (5)].qstr).data());
                ;}
    break;

  case 70:

    { (yyval.qstr)=""; ;}
    break;

  case 71:

    { 
                  QCString l=(yyvsp[(1) - (1)].qstr);
                  (yyval.qstr)=(yyvsp[(1) - (1)].qstr); 
                ;}
    break;

  case 72:

    { (yyval.qstr)="configuration"; ;}
    break;

  case 73:

    { (yyval.qstr)=(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 74:

    { (yyval.qstr)=""; ;}
    break;

  case 75:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 76:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 79:

    {
                          lastCompound=current;
                          Entry *clone=new Entry(*current);
                         clone->section=Entry::NAMESPACE_SEC;
                         clone->spec=VhdlDocGen::PACKAGE;
                         clone->name=(yyvsp[(2) - (3)].qstr);
                         clone->startLine=s_str.iLine;
                         clone->bodyLine=s_str.iLine;
                         clone->protection=Package;
                         current_root->addSubEntry(clone);
                         addVhdlType((yyvsp[(2) - (3)].qstr),s_str.iLine,Entry::CLASS_SEC,VhdlDocGen::PACKAGE,0,0,Package);
                       ;}
    break;

  case 81:

    { lastCompound=0; ;}
    break;

  case 82:

    { lastCompound=0; ;}
    break;

  case 83:

    { lastCompound=0; ;}
    break;

  case 92:

    {lastCompound=0;;}
    break;

  case 93:

    {lastCompound=0;;}
    break;

  case 94:

    {
                        (yyval.qstr)=(yyvsp[(3) - (4)].qstr);
                        lastCompound=current;
                        (yyval.qstr).prepend("_");
                        addVhdlType((yyval.qstr),getParsedLine(t_PACKAGE) ,Entry::CLASS_SEC,VhdlDocGen::PACKAGE_BODY,0,0,Protected);
                      ;}
    break;

  case 95:

    { (yyval.qstr)="";lastCompound=0; ;}
    break;

  case 96:

    { lastCompound=0; ;}
    break;

  case 97:

    { lastCompound=0; ;}
    break;

  case 98:

    { lastCompound=0; ;}
    break;

  case 99:

    { (yyval.qstr)=""; ;}
    break;

  case 106:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 107:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 108:

    {  (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 109:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 110:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 111:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 112:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 113:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 114:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 115:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 116:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 117:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 118:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 119:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 120:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 121:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 122:

    { (yyval.qstr)=""; ;}
    break;

  case 134:

    { (yyval.qstr)=""; ;}
    break;

  case 135:

    { (yyval.qstr)=""; ;}
    break;

  case 158:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 159:

    { (yyval.qstr)=""; ;}
    break;

  case 160:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 161:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 162:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 164:

    { (yyval.qstr)=""; ;}
    break;

  case 165:

    { (yyval.qstr)=""; ;}
    break;

  case 166:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 167:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 168:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 169:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 170:

    { (yyval.qstr)=(yyvsp[(3) - (4)].qstr); ;}
    break;

  case 171:

    { (yyval.qstr)="pure"; ;}
    break;

  case 172:

    { (yyval.qstr)="impure"; ;}
    break;

  case 173:

    { currP=0; ;}
    break;

  case 174:

    { 
              currP=VhdlDocGen::PROCEDURE; 
              createFunction((yyvsp[(2) - (2)].qstr),currP,0); 
              tempEntry=current;
             ;}
    break;

  case 175:

    {  newEntry(); ;}
    break;

  case 176:

    {
                currP=VhdlDocGen::FUNCTION;
                createFunction((yyvsp[(1) - (3)].qstr),currP,(yyvsp[(3) - (3)].qstr).data());
              ;}
    break;

  case 177:

    {
                tempEntry=current;
                current->type=(yyvsp[(7) - (7)].qstr);
                newEntry();
              ;}
    break;

  case 178:

    {
                currP=VhdlDocGen::FUNCTION;
                createFunction(0,currP,(yyvsp[(2) - (2)].qstr).data());
              ;}
    break;

  case 179:

    {
                tempEntry=current;
                current->type=(yyvsp[(6) - (6)].qstr);
                newEntry();

              ;}
    break;

  case 182:

    { param_sec=PARAM_SEC; ;}
    break;

  case 183:

    { param_sec= 0; ;}
    break;

  case 184:

    { param_sec=PARAM_SEC; ;}
    break;

  case 185:

    { param_sec= 0; ;}
    break;

  case 191:

    {
      if ((yyvsp[(3) - (3)].qstr).data())
      {
        FlowChart::addFlowChart(FlowChart::VARIABLE_NO,(yyvsp[(3) - (3)].qstr),0);
      }
      FlowChart::addFlowChart(FlowChart::BEGIN_NO,"BEGIN",0);
    ;}
    break;

  case 192:

    {
      tempEntry->endBodyLine=s_str.yyLineNr;
      createFlow();    
      currP=0;
    ;}
    break;

  case 193:

    {
      currP=0;
    ;}
    break;

  case 201:

    { (yyval.qstr)=""; ;}
    break;

  case 202:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 203:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 204:

    { (yyval.qstr)=""; ;}
    break;

  case 205:

    { (yyval.qstr)=""; ;}
    break;

  case 209:

    {
                    // adding generic :  [ package foo  is new bar]
                    if (parse_sec==GEN_SEC)
                    {
                       addVhdlType(current->name.data(),getParsedLine(t_PACKAGE),Entry::VARIABLE_SEC,VhdlDocGen::GENERIC,(yyvsp[(1) - (1)].qstr).data(),0);
                    }
                  ;}
    break;

  case 210:

    {
                    if (parse_sec==GEN_SEC)
                    {
                      int a=getParsedLine(t_FUNCTION);
                      int b=getParsedLine(t_PROCEDURE);

                      if (a>b) b=a;

                      addVhdlType(current->name.data(),b,Entry::VARIABLE_SEC,VhdlDocGen::GENERIC,(yyvsp[(1) - (1)].qstr).data(),0);
                    }
                  ;}
    break;

  case 211:

    {
                    if (parse_sec==GEN_SEC)
                    {
                        addVhdlType((yyvsp[(2) - (2)].qstr),s_str.iLine,Entry::VARIABLE_SEC,currP,(yyvsp[(1) - (2)].qstr).data(),0);
                    }
                  ;}
    break;

  case 212:

    {
                    (yyval.qstr)=(yyvsp[(2) - (7)].qstr)+":"+(yyvsp[(4) - (7)].qstr)+(yyvsp[(5) - (7)].qstr)+(yyvsp[(6) - (7)].qstr)+(yyvsp[(7) - (7)].qstr);
                    if (currP!=VhdlDocGen::COMPONENT)
                    {
                      if (currP==VhdlDocGen::FUNCTION || currP==VhdlDocGen::PROCEDURE)
                      {
                        addProto((yyvsp[(1) - (7)].qstr).data(),(yyvsp[(2) - (7)].qstr).data(),(yyvsp[(4) - (7)].qstr).data(),(yyvsp[(5) - (7)].qstr).data(),(yyvsp[(6) - (7)].qstr).data(),(yyvsp[(7) - (7)].qstr).data());
                      }
                      else
                      {
                        QCString i=(yyvsp[(5) - (7)].qstr)+(yyvsp[(6) - (7)].qstr)+(yyvsp[(7) - (7)].qstr);
                        if (currP==VhdlDocGen::GENERIC)
                          addVhdlType((yyvsp[(2) - (7)].qstr),s_str.iLine,Entry::VARIABLE_SEC,currP,i.data(),(yyvsp[(4) - (7)].qstr).data());
                        else if(parse_sec != GEN_SEC)
                          addVhdlType((yyvsp[(2) - (7)].qstr),s_str.iLine,Entry::VARIABLE_SEC,currP,i.data(),(yyvsp[(4) - (7)].qstr).data());
                      }
                      //   fprintf(stderr,"\n\n <<port  %s  >>\n",$$.data());
                    } // if component
                  ;}
    break;

  case 213:

    { (yyval.qstr)=""; ;}
    break;

  case 214:

    { (yyval.qstr)=":="+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 215:

    { (yyval.qstr)=""; ;}
    break;

  case 216:

    { (yyval.qstr)="buffer"; ;}
    break;

  case 217:

    { (yyval.qstr)="bus"; ;}
    break;

  case 218:

    { (yyval.qstr)=""; ;}
    break;

  case 219:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 220:

    { (yyval.qstr)=""; ;}
    break;

  case 221:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 222:

    { (yyval.qstr)="in"; ;}
    break;

  case 223:

    { (yyval.qstr)="out"; ;}
    break;

  case 224:

    { (yyval.qstr)="inout"; ;}
    break;

  case 225:

    { (yyval.qstr)="buffer"; ;}
    break;

  case 226:

    { (yyval.qstr)="link"; ;}
    break;

  case 227:

    { (yyval.qstr)="("+(yyvsp[(2) - (4)].qstr)+")"; ;}
    break;

  case 228:

    { (yyval.qstr)=""; ;}
    break;

  case 229:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 230:

    { (yyval.qstr)=", "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 231:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr);;}
    break;

  case 232:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 233:

    {
      QCString str="("+(yyvsp[(2) - (4)].qstr)+(yyvsp[(3) - (4)].qstr);
      str.append(")");
      (yyval.qstr)=str;
    ;}
    break;

  case 234:

    { (yyval.qstr)=""; ;}
    break;

  case 235:

    { (yyval.qstr)=" ( open ) "; ;}
    break;

  case 236:

    { (yyval.qstr)=""; ;}
    break;

  case 237:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 238:

    { (yyval.qstr)=","+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 239:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+"=>"+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 240:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 241:

    { (yyval.qstr)="<>"; ;}
    break;

  case 242:

    { (yyval.qstr)="default"; ;}
    break;

  case 243:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 244:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+"=>"+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 245:

    {  (yyval.qstr)=(yyvsp[(1) - (1)].qstr) ; ;}
    break;

  case 246:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 247:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 248:

    { (yyval.qstr)="open"; ;}
    break;

  case 249:

    { (yyval.qstr)="inertial"; ;}
    break;

  case 250:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 251:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 252:

    { (yyval.qstr)="sll"; ;}
    break;

  case 253:

    { (yyval.qstr)="sra"; ;}
    break;

  case 254:

    { (yyval.qstr)="sla"; ;}
    break;

  case 255:

    { (yyval.qstr)="srl"; ;}
    break;

  case 256:

    { (yyval.qstr)="ror"; ;}
    break;

  case 257:

    { (yyval.qstr)="rol"; ;}
    break;

  case 258:

    { (yyval.qstr)= (yyvsp[(1) - (3)].qstr)+(yyvsp[(2) - (3)].qstr)+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 259:

    { (yyval.qstr)= (yyvsp[(1) - (3)].qstr)+" and "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 260:

    { (yyval.qstr)= (yyvsp[(1) - (3)].qstr)+" xor "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 261:

    { (yyval.qstr)= (yyvsp[(1) - (3)].qstr)+" or "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 262:

    { (yyval.qstr)= (yyvsp[(1) - (3)].qstr)+" nor "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 263:

    { (yyval.qstr)= (yyvsp[(1) - (3)].qstr)+"xnor"+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 264:

    { (yyval.qstr)= (yyvsp[(1) - (3)].qstr)+"nand"+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 265:

    { (yyval.qstr)= (yyvsp[(1) - (3)].qstr)+"nand"+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 266:

    { (yyval.qstr)= (yyvsp[(1) - (3)].qstr)+"nor"+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 267:

    { (yyval.qstr)= (yyvsp[(1) - (3)].qstr)+"nand"+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 268:

    { (yyval.qstr)= (yyvsp[(1) - (3)].qstr)+" and "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 269:

    { (yyval.qstr)= (yyvsp[(1) - (3)].qstr)+" or "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 270:

    { (yyval.qstr)= (yyvsp[(1) - (3)].qstr)+" xor "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 271:

    { (yyval.qstr)=" ?? "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 272:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 273:

    { (yyval.qstr)="+"+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 274:

    { (yyval.qstr)="-"+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 275:

    { (yyval.qstr)="abs"+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 276:

    { (yyval.qstr)="not "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 277:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" ** "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 278:

    { (yyval.qstr)=(yyvsp[(2) - (4)].qstr)+" ** "+(yyvsp[(4) - (4)].qstr); ;}
    break;

  case 279:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" mod "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 280:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" rem "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 281:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" & "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 282:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" * "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 283:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" + "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 284:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" - "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 285:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" <= "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 286:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" >= "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 287:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" < "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 288:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" > "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 289:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" = "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 290:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" != "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 291:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" / "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 292:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" ?/= "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 293:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" ?= "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 294:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" ?< "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 295:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" ?> "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 296:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" ?<= "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 297:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" ?>= "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 298:

    { (yyval.qstr) = "-"+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 299:

    { (yyval.qstr) = "+"+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 300:

    { (yyval.qstr) = (yyvsp[(1) - (1)].qstr); ;}
    break;

  case 301:

    { (yyval.qstr) = (yyvsp[(1) - (3)].qstr)+" "+(yyvsp[(2) - (3)].qstr)+" "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 302:

    { (yyval.qstr) = "&"; ;}
    break;

  case 303:

    { (yyval.qstr) = "-"; ;}
    break;

  case 304:

    { (yyval.qstr) = "+"; ;}
    break;

  case 305:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 306:

    { (yyval.qstr) = (yyvsp[(1) - (3)].qstr)+" "+(yyvsp[(2) - (3)].qstr)+" "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 307:

    { (yyval.qstr) = "*";   ;}
    break;

  case 308:

    { (yyval.qstr) = "rem"; ;}
    break;

  case 309:

    { (yyval.qstr) = "mod"; ;}
    break;

  case 310:

    { (yyval.qstr) = "/";   ;}
    break;

  case 311:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 312:

    { (yyval.qstr)="abs "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 313:

    { (yyval.qstr)="not  "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 314:

    { (yyval.qstr) = (yyvsp[(1) - (3)].qstr)+" ** "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 315:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 316:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 317:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 318:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 319:

    { (yyval.qstr)=""; ;}
    break;

  case 320:

    { (yyval.qstr)="("+(yyvsp[(2) - (3)].qstr)+")"; ;}
    break;

  case 321:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 322:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 323:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 324:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 325:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 326:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 327:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 328:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 329:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+"."+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 330:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 331:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 332:

    { (yyval.qstr)="all"; ;}
    break;

  case 333:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 334:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 335:

    { (yyval.qstr)="'"; ;}
    break;

  case 336:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+"' "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 338:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" '"+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 339:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+"' range "; ;}
    break;

  case 340:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+"' range "; ;}
    break;

  case 341:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" ) "; ;}
    break;

  case 342:

    { (yyval.qstr)="( "+(yyvsp[(2) - (5)].qstr)+ "=>"+(yyvsp[(4) - (5)].qstr)+" ) "; ;}
    break;

  case 343:

    { (yyval.qstr)=" ( "+(yyvsp[(2) - (4)].qstr)+","+(yyvsp[(4) - (4)].qstr); ;}
    break;

  case 344:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+","+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 345:

    { (yyval.qstr)=(yyvsp[(1) - (5)].qstr)+"'("+(yyvsp[(4) - (5)].qstr)+" ) "; ;}
    break;

  case 346:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+"'"+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 354:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+"=> "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 355:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 356:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 357:

    { (yyval.qstr)="";        ;}
    break;

  case 358:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 359:

    { (yyval.qstr)=" | "+(yyvsp[(2) - (2)].qstr);  ;}
    break;

  case 360:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 361:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 362:

    { (yyval.qstr)="others"; ;}
    break;

  case 363:

    { (yyval.qstr)=""; ;}
    break;

  case 364:

    {
             addVhdlType((yyvsp[(2) - (4)].qstr),getParsedLine(t_TYPE),Entry::VARIABLE_SEC,VhdlDocGen::TYPE,0,(yyvsp[(3) - (4)].qstr).data());
            (yyval.qstr)="type ";
            (yyval.qstr)+=(yyvsp[(2) - (4)].qstr)+(yyvsp[(3) - (4)].qstr)+";";
           ;}
    break;

  case 365:

    { (yyval.qstr)=""; ;}
    break;

  case 366:

    { (yyval.qstr)=""; ;}
    break;

  case 367:

    { (yyval.qstr)="  "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 368:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 369:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 370:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 371:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 372:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 373:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 374:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 375:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 376:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 377:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 378:

    { (yyval.qstr)="( "+(yyvsp[(2) - (4)].qstr)+" "+(yyvsp[(3) - (4)].qstr)+" )"; ;}
    break;

  case 379:

    { (yyval.qstr)=""; ;}
    break;

  case 380:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 381:

    { (yyval.qstr)=","+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 382:

    {
                             (yyval.qstr)=(yyvsp[(1) - (6)].qstr);
                             current->args=(yyvsp[(3) - (6)].qstr)+"#"+(yyvsp[(4) - (6)].qstr);
                             current->args.prepend("units");
                             current->spec=VhdlDocGen::UNITS;
                           ;}
    break;

  case 385:

    { (yyval.qstr)=""; ;}
    break;

  case 386:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 387:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr)+"#"; ;}
    break;

  case 388:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr); ;}
    break;

  case 389:

    { (yyval.qstr)=(yyvsp[(1) - (4)].qstr)+"="+(yyvsp[(3) - (4)].qstr); ;}
    break;

  case 390:

    {
      QCString sr1=" array ( "+(yyvsp[(3) - (7)].qstr)+" "+(yyvsp[(4) - (7)].qstr);
      QCString sr2=" ) of "+(yyvsp[(7) - (7)].qstr);
      (yyval.qstr)=sr1+sr2;
    ;}
    break;

  case 391:

    { (yyval.qstr)=""; ;}
    break;

  case 392:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+"  "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 393:

    { (yyval.qstr)=", "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 394:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" range<> "; ;}
    break;

  case 395:

    { (yyval.qstr)=" array "+(yyvsp[(2) - (4)].qstr)+" of "+(yyvsp[(4) - (4)].qstr); ;}
    break;

  case 396:

    { (yyval.qstr)=""; ;}
    break;

  case 397:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 398:

    {
      QRegExp reg("[\\s]");
      QCString oo=(yyvsp[(2) - (6)].qstr)+" "+(yyvsp[(3) - (6)].qstr);
      current->spec=VhdlDocGen::RECORD;
      current->args=oo;
      current->args.replace(reg,"%");
      current->args.prepend("record");
      (yyval.qstr)=(yyvsp[(2) - (6)].qstr)+" "+(yyvsp[(3) - (6)].qstr);
    ;}
    break;

  case 399:

    { (yyval.qstr)=""; ;}
    break;

  case 400:

    {
      (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr);
    ;}
    break;

  case 401:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 402:

    { (yyval.qstr)=(yyvsp[(1) - (4)].qstr)+":"+(yyvsp[(3) - (4)].qstr)+"#"; ;}
    break;

  case 403:

    { (yyval.qstr)="access "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 404:

    { (yyval.qstr)="file of "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 405:

    {
      addVhdlType((yyvsp[(2) - (5)].qstr),getParsedLine(t_SUBTYPE),Entry::VARIABLE_SEC,VhdlDocGen::SUBTYPE,0,(yyvsp[(4) - (5)].qstr).data());
    ;}
    break;

  case 406:

    { (yyval.qstr)=""; ;}
    break;

  case 407:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 408:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 409:

    { (yyval.qstr)=""; ;}
    break;

  case 410:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 411:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" "+(yyvsp[(2) - (3)].qstr)+" "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 412:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 413:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" "+(yyvsp[(2) - (3)].qstr)+" "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 414:

    { (yyval.qstr)=""; ;}
    break;

  case 415:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 416:

    { (yyval.qstr)="range "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 417:

    { (yyval.qstr)="("+(yyvsp[(2) - (4)].qstr)+" "+(yyvsp[(3) - (4)].qstr)+")"; ;}
    break;

  case 418:

    { (yyval.qstr)=""; ;}
    break;

  case 419:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 420:

    { (yyval.qstr)=","+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 421:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 422:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 423:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 424:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+"  "+(yyvsp[(2) - (3)].qstr)+"  "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 425:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 426:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+"  "+(yyvsp[(2) - (3)].qstr)+"  "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 427:

    { (yyval.qstr)=" to "; ;}
    break;

  case 428:

    { (yyval.qstr)=" downto "; ;}
    break;

  case 429:

    {
                                    QCString it=(yyvsp[(4) - (6)].qstr)+" "+(yyvsp[(5) - (6)].qstr);
                                    //  fprintf(stderr,"\n currP %d \n",currP);
                                    addVhdlType((yyvsp[(2) - (6)].qstr),getParsedLine(t_CONSTANT),Entry::VARIABLE_SEC,VhdlDocGen::CONSTANT,0,it.data());
                                    (yyval.qstr)="constant "+(yyvsp[(2) - (6)].qstr);
                                    (yyval.qstr)+=": ";
                                    (yyval.qstr)+=it+";";
                                  ;}
    break;

  case 430:

    { (yyval.qstr)="";      ;}
    break;

  case 431:

    { (yyval.qstr)=":="+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 432:

    {
                                    QCString s=(yyvsp[(4) - (7)].qstr)+" "+(yyvsp[(6) - (7)].qstr);
                                    addVhdlType((yyvsp[(2) - (7)].qstr),getParsedLine(t_SIGNAL),Entry::VARIABLE_SEC,VhdlDocGen::SIGNAL,0,s.data());
                                  ;}
    break;

  case 433:

    { (yyval.qstr)=""; ;}
    break;

  case 434:

    { (yyval.qstr)=":="+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 435:

    { (yyval.qstr)=""; ;}
    break;

  case 436:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 437:

    {
                                    (yyval.qstr)=(yyvsp[(2) - (6)].qstr)+":"+(yyvsp[(4) - (6)].qstr)+" "+(yyvsp[(5) - (6)].qstr)+";";
                                    (yyval.qstr).prepend("variable: ");
                                  ;}
    break;

  case 438:

    {
                                    (yyval.qstr)=(yyvsp[(5) - (7)].qstr)+" "+(yyvsp[(6) - (7)].qstr);
                                    addVhdlType((yyvsp[(3) - (7)].qstr),getParsedLine(t_VARIABLE),Entry::VARIABLE_SEC,VhdlDocGen::SHAREDVARIABLE,0,(yyval.qstr).data());
                                  ;}
    break;

  case 439:

    { (yyval.qstr)=""; ;}
    break;

  case 440:

    { (yyval.qstr)=":="+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 441:

    { (yyval.qstr)="constant"; ;}
    break;

  case 442:

    { (yyval.qstr)="signal"; ;}
    break;

  case 443:

    { (yyval.qstr)="variable"; ;}
    break;

  case 444:

    { (yyval.qstr)="shared"; ;}
    break;

  case 445:

    { (yyval.qstr)="file"; ;}
    break;

  case 446:

    { (yyval.qstr)="type"; ;}
    break;

  case 447:

    { (yyval.qstr)="bus"; ;}
    break;

  case 448:

    { (yyval.qstr)="register"; ;}
    break;

  case 449:

    {
                                    QCString s=(yyvsp[(3) - (7)].qstr)+" is "+(yyvsp[(5) - (7)].qstr)+(yyvsp[(6) - (7)].qstr);
                                    addVhdlType((yyvsp[(2) - (7)].qstr),getParsedLine(t_ALIAS),Entry::VARIABLE_SEC,VhdlDocGen::ALIAS,0,s.data());
                                   (yyval.qstr)="alias "+(yyvsp[(2) - (7)].qstr);
                                   (yyval.qstr)+=": ";
                                   (yyval.qstr)+=s+";";
                                  ;}
    break;

  case 450:

    { (yyval.qstr)=""; ;}
    break;

  case 451:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 452:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 453:

    { (yyval.qstr)=""; ;}
    break;

  case 454:

    { (yyval.qstr)=(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 455:

    {
             addVhdlType((yyvsp[(2) - (8)].qstr),getParsedLine(t_FILE),Entry::VARIABLE_SEC,VhdlDocGen::VFILE,0,(yyvsp[(4) - (8)].qstr).data());
           ;}
    break;

  case 456:

    {
             QCString s=(yyvsp[(4) - (6)].qstr)+" "+(yyvsp[(5) - (6)].qstr);
             addVhdlType((yyvsp[(2) - (6)].qstr),getParsedLine(t_FILE),Entry::VARIABLE_SEC,VhdlDocGen::VFILE,0,s.data());
           ;}
    break;

  case 457:

    { (yyval.qstr)=""; ;}
    break;

  case 458:

    { (yyval.qstr)="open "+(yyvsp[(2) - (4)].qstr)+" is "+s_str.qstr; ;}
    break;

  case 459:

    { (yyval.qstr)=""; ;}
    break;

  case 460:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 461:

    { (yyval.qstr)="disconnect "+(yyvsp[(2) - (7)].qstr)+":"+(yyvsp[(4) - (7)].qstr)+" after "+(yyvsp[(6) - (7)].qstr); ;}
    break;

  case 462:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 463:

    { (yyval.qstr)="others"; ;}
    break;

  case 464:

    { (yyval.qstr)="all"; ;}
    break;

  case 465:

    { (yyval.qstr)=""; ;}
    break;

  case 466:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 467:

    { (yyval.qstr)=" , "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 468:

    {
                  addVhdlType((yyvsp[(2) - (5)].qstr),getParsedLine(t_ATTRIBUTE),Entry::VARIABLE_SEC,VhdlDocGen::ATTRIBUTE,0,(yyvsp[(4) - (5)].qstr).data());
                 (yyval.qstr)= "attribute "+(yyvsp[(2) - (5)].qstr)+ " : "+(yyvsp[(4) - (5)].qstr);
                ;}
    break;

  case 469:

    {
                  QCString att=(yyvsp[(4) - (7)].qstr)+" is "+(yyvsp[(6) - (7)].qstr);
                  addVhdlType((yyvsp[(2) - (7)].qstr),getParsedLine(t_ATTRIBUTE),Entry::VARIABLE_SEC,VhdlDocGen::ATTRIBUTE,0,att.data());
                  (yyval.qstr)="attribute "+att+";";
                ;}
    break;

  case 470:

    { (yyval.qstr)=(yyvsp[(1) - (4)].qstr)+(yyvsp[(2) - (4)].qstr)+":"+(yyvsp[(4) - (4)].qstr); ;}
    break;

  case 471:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 472:

    { (yyval.qstr)="others";  ;}
    break;

  case 473:

    { (yyval.qstr)="all";     ;}
    break;

  case 474:

    { (yyval.qstr)="";        ;}
    break;

  case 475:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 476:

    { (yyval.qstr)=","+(yyvsp[(2) - (2)].qstr);    ;}
    break;

  case 477:

    { (yyval.qstr)="entity";        ;}
    break;

  case 478:

    { (yyval.qstr)="architecture";  ;}
    break;

  case 479:

    { (yyval.qstr)="package";       ;}
    break;

  case 480:

    { (yyval.qstr)="configuration"; ;}
    break;

  case 481:

    { (yyval.qstr)="component";     ;}
    break;

  case 482:

    { (yyval.qstr)="label";         ;}
    break;

  case 483:

    { (yyval.qstr)="type";          ;}
    break;

  case 484:

    { (yyval.qstr)="subtype";       ;}
    break;

  case 485:

    { (yyval.qstr)="procedure";     ;}
    break;

  case 486:

    { (yyval.qstr)="function";              ;}
    break;

  case 487:

    { (yyval.qstr)="signal";        ;}
    break;

  case 488:

    { (yyval.qstr)="variable";      ;}
    break;

  case 489:

    { (yyval.qstr)="constant";      ;}
    break;

  case 490:

    { (yyval.qstr)="group";         ;}
    break;

  case 491:

    { (yyval.qstr)="file";          ;}
    break;

  case 492:

    { (yyval.qstr)="units";         ;}
    break;

  case 493:

    { (yyval.qstr)="literal";       ;}
    break;

  case 494:

    { (yyval.qstr)="sequence";      ;}
    break;

  case 495:

    { (yyval.qstr)="property";      ;}
    break;

  case 496:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 497:

    { (yyval.qstr)=""; ;}
    break;

  case 498:

    { (yyval.qstr)=""; ;}
    break;

  case 499:

    { (yyval.qstr)=""; ;}
    break;

  case 500:

    { (yyval.qstr)="else generate "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 501:

    { (yyval.qstr)="else "+(yyvsp[(2) - (4)].qstr)+" generate "+(yyvsp[(4) - (4)].qstr); ;}
    break;

  case 502:

    { (yyval.qstr)=""; ;}
    break;

  case 503:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 504:

    { (yyval.qstr)="elsif "+(yyvsp[(2) - (4)].qstr)+" generate "+(yyvsp[(4) - (4)].qstr); ;}
    break;

  case 505:

    { (yyval.qstr)="elsif "+(yyvsp[(2) - (5)].qstr)+(yyvsp[(3) - (5)].qstr)+" generate "+(yyvsp[(5) - (5)].qstr); ;}
    break;

  case 506:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 507:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 508:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 509:

    {
    if (!lab.isEmpty())
    {
      (yyval.qstr)=lab+" :for "+(yyvsp[(2) - (4)].qstr)+" in "+(yyvsp[(4) - (4)].qstr);
    }
    else
    {
      (yyval.qstr)=" for "+(yyvsp[(2) - (4)].qstr)+" in "+(yyvsp[(4) - (4)].qstr);
    }
    FlowChart::addFlowChart(FlowChart::FOR_NO,0,(yyval.qstr),lab.data());
    lab.resize(0);
  ;}
    break;

  case 510:

    {
    (yyval.qstr)=lab+" for "+(yyvsp[(2) - (5)].qstr)+(yyvsp[(3) - (5)].qstr)+" in "+(yyvsp[(5) - (5)].qstr);
    FlowChart::addFlowChart(FlowChart::FOR_NO,0,(yyval.qstr),lab.data());
    lab="";
  ;}
    break;

  case 511:

    {
                             (yyval.qstr)=" while "+(yyvsp[(2) - (2)].qstr);
                             FlowChart::addFlowChart(FlowChart::WHILE_NO,0,(yyval.qstr),lab.data());
                             lab="";
                           ;}
    break;

  case 512:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 513:

    { (yyval.qstr)=""; ;}
    break;

  case 514:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 515:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 516:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 517:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 518:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 519:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 520:

    {
                                              QCString li=(yyvsp[(1) - (1)].qstr);
                                              (yyval.qstr)=(yyvsp[(1) - (1)].qstr);
                                            ;}
    break;

  case 521:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 523:

    { pushLabel(genLabels,(yyvsp[(1) - (3)].qstr)); ;}
    break;

  case 524:

    {
              (yyval.qstr)=(yyvsp[(1) - (15)].qstr)+":block"; //+$4+$5+$6+$7+$8+"begin "+$10+" block "+$13;
              genLabels=popLabel(genLabels);
            ;}
    break;

  case 525:

    { (yyval.qstr)=""; ;}
    break;

  case 526:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 527:

    { (yyval.qstr)=""; ;}
    break;

  case 528:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 529:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 530:

    { (yyval.qstr)=""; ;}
    break;

  case 531:

    { (yyval.qstr)="port "+(yyvsp[(2) - (4)].qstr)+";"+(yyvsp[(4) - (4)].qstr); ;}
    break;

  case 532:

    { (yyval.qstr)="port map "+(yyvsp[(3) - (4)].qstr); ;}
    break;

  case 533:

    { (yyval.qstr)=""; ;}
    break;

  case 534:

    { (yyval.qstr)="generic "+(yyvsp[(2) - (4)].qstr)+";"+(yyvsp[(4) - (4)].qstr); ;}
    break;

  case 535:

    { (yyval.qstr)=""; ;}
    break;

  case 536:

    { (yyval.qstr)="generic map "+(yyvsp[(3) - (4)].qstr); ;}
    break;

  case 537:

    { (yyval.qstr)=""; ;}
    break;

  case 538:

    { (yyval.qstr)="("+(yyvsp[(2) - (4)].qstr)+")"+(yyvsp[(4) - (4)].qstr); ;}
    break;

  case 539:

    { (yyval.qstr)=""; ;}
    break;

  case 540:

    { (yyval.qstr)=" is "; ;}
    break;

  case 541:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 542:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+"."+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 543:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 544:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 545:

    { (yyval.qstr)="("+(yyvsp[(2) - (3)].qstr)+")"; ;}
    break;

  case 546:

    { (yyval.qstr)="configuration";yyLineNr=s_str.iLine; ;}
    break;

  case 547:

    { (yyval.qstr)="entity";yyLineNr=s_str.iLine; ;}
    break;

  case 548:

    { (yyval.qstr)="component";yyLineNr=s_str.iLine; ;}
    break;

  case 549:

    { yyLineNr=s_str.iLine; ;}
    break;

  case 550:

    {
                                 addCompInst((yyvsp[(1) - (9)].qstr).lower().data(),(yyvsp[(3) - (9)].qstr).lower().data(),0,yyLineNr);(yyval.qstr)="";
                               ;}
    break;

  case 551:

    { yyLineNr=s_str.iLine; ;}
    break;

  case 552:

    {
                                 addCompInst((yyvsp[(1) - (8)].qstr).lower().data(),(yyvsp[(3) - (8)].qstr).lower().data(),0,yyLineNr);(yyval.qstr)="222";
                               ;}
    break;

  case 553:

    {
                                 addCompInst((yyvsp[(1) - (8)].qstr).lower().data(),(yyvsp[(4) - (8)].qstr).lower().data(),(yyvsp[(3) - (8)].qstr).data(),yyLineNr);(yyval.qstr)="";
                               ;}
    break;

  case 554:

    {
                                 addCompInst((yyvsp[(1) - (9)].qstr).lower().data(),(yyvsp[(4) - (9)].qstr).lower().data(),(yyvsp[(3) - (9)].qstr).lower().data(),yyLineNr);(yyval.qstr)="";
                               ;}
    break;

  case 557:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+":"+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 558:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 559:

    { (yyval.qstr)=(yyvsp[(1) - (4)].qstr)+":"+"postponed "+(yyvsp[(4) - (4)].qstr); ;}
    break;

  case 560:

    { (yyval.qstr)="postponed "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 561:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+":"+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 562:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 563:

    { (yyval.qstr)=(yyvsp[(1) - (4)].qstr)+":"+"postponed "+(yyvsp[(4) - (4)].qstr); ;}
    break;

  case 564:

    { (yyval.qstr)="postponed "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 565:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+":"+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 566:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 567:

    { (yyval.qstr)=(yyvsp[(1) - (4)].qstr)+":"+"postponed "+(yyvsp[(4) - (4)].qstr); ;}
    break;

  case 568:

    { (yyval.qstr)="postponed "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 569:

    { (yyval.qstr)=(yyvsp[(1) - (4)].qstr)+":"+"postponed "+(yyvsp[(4) - (4)].qstr); ;}
    break;

  case 570:

    { (yyval.qstr)="postponed "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 571:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+":"+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 572:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 573:

    { (yyval.qstr)=(yyvsp[(1) - (5)].qstr)+"<="+(yyvsp[(3) - (5)].qstr)+(yyvsp[(4) - (5)].qstr); ;}
    break;

  case 574:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 575:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" when "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 576:

    { (yyval.qstr)=(yyvsp[(1) - (5)].qstr)+" when "+(yyvsp[(3) - (5)].qstr)+"else"+(yyvsp[(5) - (5)].qstr); ;}
    break;

  case 577:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 578:

    { (yyval.qstr)="unaffected"; ;}
    break;

  case 579:

    { (yyval.qstr)=""; ;}
    break;

  case 580:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 581:

    { (yyval.qstr)=","+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 582:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 583:

    { (yyval.qstr)=""; ;}
    break;

  case 584:

    { (yyval.qstr)="after "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 585:

    { (yyval.qstr)=" null "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 586:

    { (yyval.qstr)=" null "; ;}
    break;

  case 587:

    { (yyval.qstr)="after "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 588:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 589:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 590:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 591:

    { (yyval.qstr)=""; ;}
    break;

  case 592:

    { (yyval.qstr)="transport "; ;}
    break;

  case 593:

    { (yyval.qstr)="transport"+(yyvsp[(2) - (3)].qstr)+" intertial "; ;}
    break;

  case 594:

    { (yyval.qstr)=" intertial "; ;}
    break;

  case 595:

    { (yyval.qstr)=""; ;}
    break;

  case 596:

    { (yyval.qstr)=" guarded "; ;}
    break;

  case 597:

    { (yyval.qstr)="with "+(yyvsp[(2) - (8)].qstr)+" select "+(yyvsp[(4) - (8)].qstr)+"<="+(yyvsp[(6) - (8)].qstr)+(yyvsp[(7) - (8)].qstr); ;}
    break;

  case 598:

    { (yyval.qstr)=(yyvsp[(1) - (4)].qstr)+(yyvsp[(2) - (4)].qstr); ;}
    break;

  case 599:

    { (yyval.qstr)=""; ;}
    break;

  case 600:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 601:

    { (yyval.qstr)=(yyvsp[(1) - (4)].qstr)+" when "+(yyvsp[(3) - (4)].qstr); ;}
    break;

  case 602:

    { (yyval.qstr)=""; ;}
    break;

  case 603:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" begin "; ;}
    break;

  case 604:

    { (yyval.qstr)="begin "; ;}
    break;

  case 606:

    { pushLabel(genLabels,(yyvsp[(1) - (2)].qstr)); ;}
    break;

  case 608:

    { genLabels=popLabel(genLabels); ;}
    break;

  case 609:

    {genLabels=popLabel(genLabels); ;}
    break;

  case 610:

    { pushLabel(genLabels,(yyvsp[(1) - (2)].qstr)); ;}
    break;

  case 613:

    { (yyval.qstr)=""; ;}
    break;

  case 614:

    { (yyval.qstr)=(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 615:

    { (yyval.qstr)="end"; ;}
    break;

  case 616:

    { (yyval.qstr)="end "+(yyvsp[(2) - (3)].qstr); ;}
    break;

  case 617:

    {  
                 current->name=(yyvsp[(1) - (3)].qstr);
                 tempEntry=current;
                 current->endBodyLine=s_str.yyLineNr;
                 newEntry();
                 currName=(yyvsp[(1) - (3)].qstr);
               ;}
    break;

  case 618:

    {
                 current->name=VhdlDocGen::getProcessNumber();
                 current->endBodyLine=s_str.yyLineNr;
                 tempEntry=current;
                 newEntry();
               ;}
    break;

  case 619:

    {  
               currP=VhdlDocGen::PROCESS; 
               current->startLine=s_str.yyLineNr;
               current->bodyLine=s_str.yyLineNr;
               ;}
    break;

  case 620:

    {
                 if ((yyvsp[(5) - (5)].qstr).data())
                  FlowChart::addFlowChart(FlowChart::VARIABLE_NO,(yyvsp[(5) - (5)].qstr).data(),0);
                FlowChart::addFlowChart(FlowChart::BEGIN_NO,"BEGIN",0);
               ;}
    break;

  case 621:

    { 
                (yyvsp[(5) - (11)].qstr).stripPrefix((yyvsp[(4) - (11)].qstr).data());
                tempEntry=current;
                currP=0;
                createFunction(currName,VhdlDocGen::PROCESS,(yyvsp[(4) - (11)].qstr).data());
                createFlow();
                currName="";
               ;}
    break;

  case 622:

    { currP=0; ;}
    break;

  case 625:

    { (yyval.qstr)=""; ;}
    break;

  case 626:

    { (yyval.qstr)="postponed"; ;}
    break;

  case 627:

    { (yyval.qstr)=""; ;}
    break;

  case 628:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 629:

    { (yyval.qstr)=""; ;}
    break;

  case 630:

    { (yyval.qstr)=""; ;}
    break;

  case 631:

    { (yyval.qstr)+=(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 632:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 633:

    { (yyval.qstr)=""; ;}
    break;

  case 634:

    { (yyval.qstr)="all"; ;}
    break;

  case 635:

    { (yyval.qstr)=(yyvsp[(2) - (3)].qstr); ;}
    break;

  case 636:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 637:

    { (yyval.qstr)=""; ;}
    break;

  case 638:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 639:

    { (yyval.qstr)=","+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 640:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 641:

    { (yyval.qstr)=""; ;}
    break;

  case 642:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 643:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 644:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr);    FlowChart::addFlowChart(FlowChart::TEXT_NO,(yyval.qstr).data(),0); ;}
    break;

  case 645:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); FlowChart::addFlowChart(FlowChart::TEXT_NO,(yyval.qstr).data(),0); ;}
    break;

  case 646:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 647:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 648:

    { (yyval.qstr)=""; ;}
    break;

  case 649:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 650:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 651:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); FlowChart::addFlowChart(FlowChart::TEXT_NO,(yyval.qstr).data(),0); ;}
    break;

  case 652:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); FlowChart::addFlowChart(FlowChart::TEXT_NO,(yyval.qstr).data(),0); ;}
    break;

  case 653:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); FlowChart::addFlowChart(FlowChart::RETURN_NO,(yyval.qstr).data(),0); ;}
    break;

  case 654:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 655:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); FlowChart::addFlowChart(FlowChart::TEXT_NO,(yyval.qstr).data(),0); ;}
    break;

  case 656:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); FlowChart::addFlowChart(FlowChart::TEXT_NO,(yyval.qstr).data(),0); ;}
    break;

  case 657:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); FlowChart::addFlowChart(FlowChart::TEXT_NO,(yyval.qstr).data(),0); ;}
    break;

  case 658:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); FlowChart::addFlowChart(FlowChart::TEXT_NO,(yyval.qstr).data(),0); ;}
    break;

  case 659:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); FlowChart::addFlowChart(FlowChart::TEXT_NO,(yyval.qstr).data(),0); ;}
    break;

  case 660:

    { (yyval.qstr)=(yyvsp[(1) - (5)].qstr)+"report "+(yyvsp[(3) - (5)].qstr)+(yyvsp[(4) - (5)].qstr)+";";  ;}
    break;

  case 661:

    { (yyval.qstr)="assert "+(yyvsp[(2) - (5)].qstr)+(yyvsp[(3) - (5)].qstr)+(yyvsp[(4) - (5)].qstr)+";"; ;}
    break;

  case 662:

    { (yyval.qstr)=""; ;}
    break;

  case 663:

    { (yyval.qstr)=" serverity "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 664:

    { (yyval.qstr)=""; ;}
    break;

  case 665:

    { (yyval.qstr)=" report "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 666:

    { (yyval.qstr)=""; ;}
    break;

  case 667:

    { (yyval.qstr)="?"; ;}
    break;

  case 668:

    { (yyval.qstr)=""; ;}
    break;

  case 669:

    { (yyval.qstr)="?"; ;}
    break;

  case 670:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 671:

    {
                     QCString ca="case "+(yyvsp[(2) - (3)].qstr)+(yyvsp[(3) - (3)].qstr);
                     FlowChart::addFlowChart(FlowChart::CASE_NO,0,ca);
                   ;}
    break;

  case 672:

    { 
                     FlowChart::moveToPrevLevel();
                     FlowChart::addFlowChart(FlowChart::END_CASE,"end case",0);
                   ;}
    break;

  case 673:

    {
                     QCString ca="case "+(yyvsp[(3) - (4)].qstr)+(yyvsp[(4) - (4)].qstr);
                     FlowChart::addFlowChart(FlowChart::CASE_NO,0,ca);
                   ;}
    break;

  case 674:

    {
                     FlowChart::moveToPrevLevel();
             
                     FlowChart::addFlowChart(FlowChart::END_CASE,0,0);
                   ;}
    break;

  case 675:

    { (yyval.qstr)=""; ;}
    break;

  case 676:

    { (yyval.qstr)=""; ;}
    break;

  case 677:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 678:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 679:

    { 
    QCString t="when ";
    t+=(yyvsp[(2) - (3)].qstr)+"=> ";
    FlowChart::addFlowChart(FlowChart::WHEN_NO,(yyvsp[(2) - (3)].qstr).data(),t);
  ;}
    break;

  case 680:

    { (yyval.qstr)=""; FlowChart::moveToPrevLevel(); ;}
    break;

  case 681:

    {
    (yyvsp[(2) - (3)].qstr).prepend("if ");
    FlowChart::addFlowChart(FlowChart::IF_NO,0,(yyvsp[(2) - (3)].qstr));
  ;}
    break;

  case 682:

    {
    FlowChart::moveToPrevLevel();
    FlowChart::addFlowChart(FlowChart::ENDIF_NO,0,0);
  ;}
    break;

  case 683:

    { (yyval.qstr)=""; ;}
    break;

  case 684:

    {
    FlowChart::addFlowChart(FlowChart::ELSE_NO,0,0);
  ;}
    break;

  case 685:

    { (yyval.qstr)=""; ;}
    break;

  case 686:

    { (yyval.qstr)=""; ;}
    break;

  case 687:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 688:

    { 
    (yyvsp[(2) - (3)].qstr).prepend("elsif ");
    FlowChart::addFlowChart(FlowChart::ELSIF_NO,0,(yyvsp[(2) - (3)].qstr).data());
  ;}
    break;

  case 689:

    { (yyval.qstr)=""; ;}
    break;

  case 690:

    {
    (yyval.qstr)=(yyvsp[(1) - (8)].qstr)+(yyvsp[(2) - (8)].qstr)+" loop "+(yyvsp[(4) - (8)].qstr)+" end loop" +(yyvsp[(7) - (8)].qstr);
    QCString endLoop="end loop" + (yyvsp[(7) - (8)].qstr);
    FlowChart::moveToPrevLevel();
    FlowChart::addFlowChart(FlowChart::END_LOOP,endLoop.data(),0);
  ;}
    break;

  case 691:

    { (yyval.qstr)=""; ;}
    break;

  case 692:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 693:

    { (yyval.qstr)=""; 
                           FlowChart::addFlowChart(FlowChart::LOOP_NO,0,"infinite loop");
                         ;}
    break;

  case 695:

    { (yyval.qstr)=""; ;}
    break;

  case 696:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+":";lab=(yyvsp[(1) - (2)].qstr); ;}
    break;

  case 697:

    {
                         FlowChart::addFlowChart(FlowChart::EXIT_NO,"exit",(yyvsp[(4) - (5)].qstr).data(),(yyvsp[(3) - (5)].qstr).data()); 
                         lab.resize(0);
                       ;}
    break;

  case 698:

    { (yyval.qstr)=""; ;}
    break;

  case 699:

    { (yyval.qstr)="when "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 700:

    { (yyval.qstr)=""; ;}
    break;

  case 701:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr);lab=(yyval.qstr); ;}
    break;

  case 702:

    {
                         FlowChart::addFlowChart(FlowChart::NEXT_NO,"next ",(yyvsp[(4) - (5)].qstr).data(),(yyvsp[(3) - (5)].qstr).data()); 
                         lab.resize(0);
                       ;}
    break;

  case 703:

    { (yyval.qstr)=""; ;}
    break;

  case 704:

    { (yyval.qstr)="when "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 705:

    { (yyval.qstr)=""; ;}
    break;

  case 706:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr);lab=(yyval.qstr); ;}
    break;

  case 707:

    { (yyval.qstr)="null"; (yyval.qstr)+=";"; ;}
    break;

  case 708:

    {
   (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+";"; 
 ;}
    break;

  case 709:

    { (yyval.qstr)="return "+(yyvsp[(2) - (3)].qstr)+";"  ; ;}
    break;

  case 710:

    { (yyval.qstr)=""; ;}
    break;

  case 711:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 712:

    { (yyval.qstr)=(yyvsp[(1) - (4)].qstr)+" <="+(yyvsp[(3) - (4)].qstr)+";"  ; ;}
    break;

  case 713:

    { (yyval.qstr)=(yyvsp[(1) - (5)].qstr)+ "<= "+(yyvsp[(3) - (5)].qstr)+(yyvsp[(4) - (5)].qstr) +";"; ;}
    break;

  case 714:

    { (yyval.qstr)=(yyvsp[(1) - (6)].qstr)+ "<= "+ " force  "+(yyvsp[(4) - (6)].qstr)+";" ; ;}
    break;

  case 715:

    { (yyval.qstr)=(yyvsp[(1) - (5)].qstr)+ "<= "+" release "+(yyvsp[(4) - (5)].qstr) +";"; ;}
    break;

  case 716:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 717:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 718:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+";"; ;}
    break;

  case 719:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 720:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr); ;}
    break;

  case 721:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 722:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+":"; ;}
    break;

  case 723:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+":="+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 724:

    { (yyval.qstr)=(yyvsp[(1) - (4)].qstr)+(yyvsp[(2) - (4)].qstr)+":="+(yyvsp[(4) - (4)].qstr); ;}
    break;

  case 725:

    {
                     (yyval.qstr)="wait "+(yyvsp[(2) - (5)].qstr)+(yyvsp[(3) - (5)].qstr)+(yyvsp[(4) - (5)].qstr)+";";
                  ;}
    break;

  case 726:

    { (yyval.qstr)=""; ;}
    break;

  case 727:

    { (yyval.qstr)="for "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 728:

    { (yyval.qstr)=""; ;}
    break;

  case 729:

    { (yyval.qstr)=" until "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 730:

    { (yyval.qstr)=""; ;}
    break;

  case 731:

    { (yyval.qstr)=" on "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 732:

    { lastEntity=0; lastCompound=0; genLabels.resize(0); ;}
    break;

  case 734:

    { lastCompound=0; genLabels.resize(0); ;}
    break;

  case 735:

    { lastEntity=0;lastCompound=0; genLabels.resize(0); ;}
    break;

  case 736:

    { lastEntity=0; lastCompound=0; genLabels.resize(0); ;}
    break;

  case 737:

    { currP=VhdlDocGen::COMPONENT; ;}
    break;

  case 738:

    { currP=VhdlDocGen::COMPONENT; ;}
    break;

  case 739:

    {
             addVhdlType((yyvsp[(2) - (7)].qstr),getParsedLine(t_COMPONENT),Entry::VARIABLE_SEC,VhdlDocGen::COMPONENT,0,0);
             currP=0;
           ;}
    break;

  case 740:

    { (yyval.qstr)=""; ;}
    break;

  case 741:

    { (yyval.qstr)=(yyvsp[(2) - (3)].qstr); ;}
    break;

  case 742:

    { (yyval.qstr)=""; ;}
    break;

  case 743:

    { (yyval.qstr)=(yyvsp[(2) - (3)].qstr); ;}
    break;

  case 744:

    { levelCounter--; ;}
    break;

  case 745:

    {
          ;}
    break;

  case 746:

    { (yyval.qstr)=""; ;}
    break;

  case 747:

    { (yyval.qstr)=""; ;}
    break;

  case 748:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+"  "; ;}
    break;

  case 749:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 750:

    { (yyval.qstr)=""; ;}
    break;

  case 751:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr); ;}
    break;

  case 752:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 753:

    {
      (yyval.qstr)=(yyvsp[(1) - (1)].qstr);
      if (levelCounter==0)
        addConfigureNode((yyvsp[(1) - (1)].qstr).data(),NULL,TRUE,FALSE);
      else
        addConfigureNode((yyvsp[(1) - (1)].qstr).data(),NULL,FALSE,FALSE);
        levelCounter++;
    ;}
    break;

  case 754:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 755:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 756:

    {
               (yyval.qstr)=(yyvsp[(2) - (7)].qstr)+" "+(yyvsp[(3) - (7)].qstr)+" "+(yyvsp[(4) - (7)].qstr);
             ;}
    break;

  case 757:

    { (yyval.qstr)=""; ;}
    break;

  case 758:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 759:

    { (yyval.qstr)=""; ;}
    break;

  case 760:

    { 
  (yyval.qstr)=""; 
;}
    break;

  case 761:

    { (yyval.qstr)=""; ;}
    break;

  case 762:

    {
               addConfigureNode(compSpec.data(),(yyvsp[(2) - (3)].qstr).data(),FALSE,TRUE);
             ;}
    break;

  case 763:

    { 
               addConfigureNode((yyvsp[(2) - (4)].qstr).data(),(yyvsp[(3) - (4)].qstr).data(),TRUE,FALSE,TRUE);
             ;}
    break;

  case 764:

    { 
               addConfigureNode((yyvsp[(2) - (7)].qstr).data(),(yyvsp[(3) - (7)].qstr).data(),TRUE,FALSE,TRUE);
              ;}
    break;

  case 765:

    { 
                                                     (yyval.qstr)=(yyvsp[(2) - (2)].qstr);
                                                   ;}
    break;

  case 766:

    { 
  (yyval.qstr)="";
;}
    break;

  case 767:

    { 
                          (yyval.qstr)="";
                           ;}
    break;

  case 768:

    {
               (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+":"+(yyvsp[(3) - (3)].qstr);
               compSpec=(yyval.qstr);
             ;}
    break;

  case 769:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 770:

    { (yyval.qstr)="all"; ;}
    break;

  case 771:

    { (yyval.qstr)="others"; ;}
    break;

  case 772:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr); ;}
    break;

  case 773:

    { (yyval.qstr)=""; ;}
    break;

  case 774:

    { (yyval.qstr)="port map "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 775:

    { (yyval.qstr)=""; ;}
    break;

  case 776:

    { (yyval.qstr)="generic map "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 777:

    { (yyval.qstr)="entity "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 778:

    { (yyval.qstr)="configuration "+ (yyvsp[(2) - (2)].qstr); ;}
    break;

  case 779:

    { (yyval.qstr)="open "; ;}
    break;

  case 780:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 781:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 782:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 783:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+","+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 784:

    {
                      // $$=$2+":"+$4+$6;
                      (yyval.qstr)="("+(yyvsp[(4) - (8)].qstr)+(yyvsp[(6) - (8)].qstr)+")";
                      addVhdlType((yyvsp[(2) - (8)].qstr),getParsedLine(t_GROUP),Entry::VARIABLE_SEC,VhdlDocGen::GROUP,(yyval.qstr).data(),0);
                    ;}
    break;

  case 785:

    {
                      (yyval.qstr)=(yyvsp[(2) - (7)].qstr)+":"+(yyvsp[(5) - (7)].qstr);
                      addVhdlType((yyvsp[(2) - (7)].qstr),getParsedLine(t_GROUP),Entry::VARIABLE_SEC,VhdlDocGen::GROUP,(yyvsp[(5) - (7)].qstr).data(),0);
                    ;}
    break;

  case 786:

    { (yyval.qstr)=""; ;}
    break;

  case 787:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 788:

    { (yyval.qstr)="";   ;}
    break;

  case 789:

    { (yyval.qstr)="<>"; ;}
    break;

  case 790:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 791:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+","+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 792:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 793:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 794:

    {
      (yyval.qstr)=s_str.qstr;
    ;}
    break;

  case 795:

    {
      (yyval.qstr)=s_str.qstr;
    ;}
    break;

  case 796:

    {
      (yyval.qstr)=s_str.qstr;
    ;}
    break;

  case 797:

    {
      (yyval.qstr)=s_str.qstr;
    ;}
    break;

  case 798:

    {
      (yyval.qstr)=s_str.qstr;
    ;}
    break;

  case 799:

    { (yyval.qstr)=""; ;}
    break;

  case 800:

    { (yyval.qstr)=""; ;}
    break;

  case 810:

    { (yyval.qstr)=""; ;}
    break;

  case 811:

    { (yyval.qstr)=""; ;}
    break;

  case 818:

    { (yyval.qstr)="context "+(yyvsp[(2) - (3)].qstr); ;}
    break;

  case 819:

    { parse_sec=CONTEXT_SEC; ;}
    break;

  case 820:

    {
                          parse_sec=0;
                          QCString v=(yyvsp[(5) - (8)].qstr);
                          addVhdlType((yyvsp[(2) - (8)].qstr),getParsedLine(t_LIBRARY),Entry::VARIABLE_SEC,VhdlDocGen::LIBRARY,"context",(yyvsp[(5) - (8)].qstr).data());
                        ;}
    break;

  case 821:

    {
                          addVhdlType((yyvsp[(2) - (6)].qstr),getParsedLine(t_LIBRARY),Entry::VARIABLE_SEC,VhdlDocGen::LIBRARY,"context",0);
                        ;}
    break;

  case 824:

    { (yyval.qstr) = (yyvsp[(1) - (1)].qstr); ;}
    break;

  case 825:

    { (yyval.qstr) = (yyvsp[(1) - (2)].qstr)+"#"+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 826:

    { (yyval.qstr) = (yyvsp[(1) - (1)].qstr); ;}
    break;

  case 827:

    { (yyval.qstr) = (yyvsp[(1) - (1)].qstr); ;}
    break;

  case 828:

    { (yyval.qstr) = (yyvsp[(1) - (1)].qstr); ;}
    break;

  case 829:

    {
      (yyval.qstr)=" is new "+(yyvsp[(5) - (7)].qstr)+(yyvsp[(6) - (7)].qstr);
      //Entry * pp=lastCompound;
      //Entry * pps=lastEntity  ;
      //assert(false);
      addVhdlType((yyvsp[(2) - (7)].qstr),getParsedLine(t_PACKAGE),Entry::VARIABLE_SEC,VhdlDocGen::INSTANTIATION,"package",(yyval.qstr).data());
    ;}
    break;

  case 830:

    {
      (yyval.qstr)=" is new "+(yyvsp[(5) - (8)].qstr)+(yyvsp[(6) - (8)].qstr);
      addVhdlType((yyvsp[(2) - (8)].qstr),getParsedLine(t_PACKAGE),Entry::VARIABLE_SEC,VhdlDocGen::INSTANTIATION,"package",(yyval.qstr).data());
    ;}
    break;

  case 831:

    { (yyval.qstr)=""; ;}
    break;

  case 832:

    {
      (yyval.qstr)= " is new "+(yyvsp[(5) - (7)].qstr)+(yyvsp[(6) - (7)].qstr);
      addVhdlType((yyvsp[(2) - (7)].qstr),getParsedLine(t_FUNCTION),Entry::VARIABLE_SEC,VhdlDocGen::INSTANTIATION,"function ",(yyval.qstr).data());
    ;}
    break;

  case 833:

    {
      (yyval.qstr)=" is new "+(yyvsp[(5) - (8)].qstr)+(yyvsp[(6) - (8)].qstr);
      addVhdlType((yyvsp[(2) - (8)].qstr),getParsedLine(t_FUNCTION),Entry::VARIABLE_SEC,VhdlDocGen::INSTANTIATION,"function ",(yyval.qstr).data());
    ;}
    break;

  case 834:

    { (yyval.qstr)=""; ;}
    break;

  case 835:

    { (yyval.qstr)=""; ;}
    break;

  case 836:

    { (yyval.qstr)="["+(yyvsp[(2) - (3)].qstr)+" ]"; ;}
    break;

  case 837:

    { (yyval.qstr)="[ ]"; ;}
    break;

  case 838:

    { (yyval.qstr)="return "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 839:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 840:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+" return "+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 841:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 842:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+" "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 843:

    { (yyval.qstr)=" , "+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 853:

    { (yyval.qstr)=""; ;}
    break;

  case 854:

    { (yyval.qstr)=""; ;}
    break;

  case 864:

    { (yyval.qstr)=""; ;}
    break;

  case 865:

    { (yyval.qstr)=""; ;}
    break;

  case 873:

    { (yyval.qstr)=""; ;}
    break;

  case 874:

    { (yyval.qstr)=" in "; ;}
    break;

  case 875:

    { (yyval.qstr)="out"; ;}
    break;

  case 876:

    { (yyval.qstr)=" transport "; ;}
    break;

  case 877:

    { (yyval.qstr)=" reject "+(yyvsp[(2) - (3)].qstr)+"inertial "; ;}
    break;

  case 878:

    { (yyval.qstr)=" inertial "; ;}
    break;

  case 884:

    { (yyval.qstr)=""; ;}
    break;

  case 890:

    { (yyval.qstr) = (yyvsp[(1) - (1)].qstr); ;}
    break;

  case 891:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 892:

    { (yyval.qstr) = "procedure "+(yyvsp[(2) - (3)].qstr)+(yyvsp[(3) - (3)].qstr); current->name=(yyvsp[(2) - (3)].qstr); ;}
    break;

  case 893:

    {
      QCString s=(yyvsp[(6) - (6)].qstr);
      if (!s.isEmpty())
      {
        s.prepend(" is ");
      }
      (yyval.qstr)=" function "+(yyvsp[(2) - (6)].qstr)+(yyvsp[(3) - (6)].qstr)+(yyvsp[(5) - (6)].qstr)+s;
      current->name=(yyvsp[(2) - (6)].qstr);
    ;}
    break;

  case 894:

    {
      QCString s=(yyvsp[(7) - (7)].qstr);
      if (!s.isEmpty())
      {
        s.prepend(" is ");
      }
      (yyval.qstr)=(yyvsp[(1) - (7)].qstr)+" function "+(yyvsp[(3) - (7)].qstr)+(yyvsp[(4) - (7)].qstr)+" return "+(yyvsp[(6) - (7)].qstr)+s;
      current->name=(yyvsp[(3) - (7)].qstr);
    ;}
    break;

  case 895:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 896:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 897:

    { (yyval.qstr)="";   ;}
    break;

  case 898:

    { (yyval.qstr)=(yyvsp[(2) - (2)].qstr);   ;}
    break;

  case 899:

    { (yyval.qstr)="<>"; ;}
    break;

  case 900:

    { (yyval.qstr)=""; ;}
    break;

  case 901:

    { (yyval.qstr)="parameter "; ;}
    break;

  case 902:

    { parse_sec=PARAM_SEC; ;}
    break;

  case 903:

    { parse_sec=0; ;}
    break;

  case 904:

    { (yyval.qstr)="("+(yyvsp[(2) - (4)].qstr)+")"; ;}
    break;

  case 905:

    {
                          (yyval.qstr)="package "+(yyvsp[(2) - (5)].qstr)+" is new "+(yyvsp[(5) - (5)].qstr);
                          current->name=(yyvsp[(2) - (5)].qstr);
                        ;}
    break;

  case 906:

    { 
                          (yyval.qstr)="package "+(yyvsp[(2) - (6)].qstr)+" is new "+(yyvsp[(5) - (6)].qstr)+"( ... )" ; 
                          current->name=(yyvsp[(2) - (6)].qstr); 
                        ;}
    break;

  case 908:

    {
                          //int u=s_str.iLine;
                          parse_sec=GEN_SEC;
                        ;}
    break;

  case 909:

    {
                          QCString vo=(yyvsp[(3) - (3)].qstr);
                          parse_sec=0;
                        ;}
    break;

  case 910:

    {
                          QCString s="<<"+(yyvsp[(2) - (6)].qstr);
                          QCString s1=(yyvsp[(3) - (6)].qstr)+":"+(yyvsp[(5) - (6)].qstr)+">>";
                          (yyval.qstr)=s+s1;
                        ;}
    break;

  case 911:

    { (yyval.qstr)="constant "; ;}
    break;

  case 912:

    { (yyval.qstr)="signal ";   ;}
    break;

  case 913:

    { (yyval.qstr)="variable "; ;}
    break;

  case 914:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 915:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 916:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 917:

    { (yyval.qstr)="."+(yyvsp[(2) - (3)].qstr)+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 918:

    { (yyval.qstr)="."+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 919:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+(yyvsp[(2) - (3)].qstr)+(yyvsp[(3) - (3)].qstr); ;}
    break;

  case 920:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 921:

    { (yyval.qstr)="^."; ;}
    break;

  case 922:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+"^."; ;}
    break;

  case 923:

    { (yyval.qstr)=(yyvsp[(1) - (1)].qstr); ;}
    break;

  case 924:

    { (yyval.qstr)=(yyvsp[(1) - (4)].qstr)+"("+(yyvsp[(3) - (4)].qstr)+")"; ;}
    break;

  case 925:

    { (yyval.qstr)=(yyvsp[(1) - (2)].qstr)+"."; ;}
    break;

  case 926:

    { (yyval.qstr)=(yyvsp[(1) - (3)].qstr)+(yyvsp[(2) - (3)].qstr)+"."; ;}
    break;

  case 927:

    { (yyval.qstr)="@"+(yyvsp[(2) - (2)].qstr); ;}
    break;

  case 928:

    {
// fprintf(stderr,"\n  tooldir %s",s_str.qstr.data() );
;}
    break;


/* Line 1267 of yacc.c.  */

      default: break;
    }
  YY_SYMBOL_PRINT ("-> $$ =", yyr1[yyn], &yyval, &yyloc);

  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);

  *++yyvsp = yyval;


  /* Now `shift' the result of the reduction.  Determine what state
     that goes to, based on the state we popped back to and the rule
     number reduced by.  */

  yyn = yyr1[yyn];

  yystate = yypgoto[yyn - YYNTOKENS] + *yyssp;
  if (0 <= yystate && yystate <= YYLAST && yycheck[yystate] == *yyssp)
    yystate = yytable[yystate];
  else
    yystate = yydefgoto[yyn - YYNTOKENS];

  goto yynewstate;


/*------------------------------------.
| yyerrlab -- here on detecting error |
`------------------------------------*/
yyerrlab:
  /* If not already recovering from an error, report this error.  */
  if (!yyerrstatus)
    {
      ++yynerrs;
#if ! YYERROR_VERBOSE
      yyerror (YY_("syntax error"));
#else
      {
	YYSIZE_T yysize = yysyntax_error (0, yystate, yychar);
	if (yymsg_alloc < yysize && yymsg_alloc < YYSTACK_ALLOC_MAXIMUM)
	  {
	    YYSIZE_T yyalloc = 2 * yysize;
	    if (! (yysize <= yyalloc && yyalloc <= YYSTACK_ALLOC_MAXIMUM))
	      yyalloc = YYSTACK_ALLOC_MAXIMUM;
	    if (yymsg != yymsgbuf)
	      YYSTACK_FREE (yymsg);
	    yymsg = (char *) YYSTACK_ALLOC (yyalloc);
	    if (yymsg)
	      yymsg_alloc = yyalloc;
	    else
	      {
		yymsg = yymsgbuf;
		yymsg_alloc = sizeof yymsgbuf;
	      }
	  }

	if (0 < yysize && yysize <= yymsg_alloc)
	  {
	    (void) yysyntax_error (yymsg, yystate, yychar);
	    yyerror (yymsg);
	  }
	else
	  {
	    yyerror (YY_("syntax error"));
	    if (yysize != 0)
	      goto yyexhaustedlab;
	  }
      }
#endif
    }



  if (yyerrstatus == 3)
    {
      /* If just tried and failed to reuse look-ahead token after an
	 error, discard it.  */

      if (yychar <= YYEOF)
	{
	  /* Return failure if at end of input.  */
	  if (yychar == YYEOF)
	    YYABORT;
	}
      else
	{
	  yydestruct ("Error: discarding",
		      yytoken, &yylval);
	  yychar = YYEMPTY;
	}
    }

  /* Else will try to reuse look-ahead token after shifting the error
     token.  */
  goto yyerrlab1;


/*---------------------------------------------------.
| yyerrorlab -- error raised explicitly by YYERROR.  |
`---------------------------------------------------*/
yyerrorlab:

  /* Pacify compilers like GCC when the user code never invokes
     YYERROR and the label yyerrorlab therefore never appears in user
     code.  */
  if (/*CONSTCOND*/ 0)
     goto yyerrorlab;

  /* Do not reclaim the symbols of the rule which action triggered
     this YYERROR.  */
  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);
  yystate = *yyssp;
  goto yyerrlab1;


/*-------------------------------------------------------------.
| yyerrlab1 -- common code for both syntax error and YYERROR.  |
`-------------------------------------------------------------*/
yyerrlab1:
  yyerrstatus = 3;	/* Each real token shifted decrements this.  */

  for (;;)
    {
      yyn = yypact[yystate];
      if (yyn != YYPACT_NINF)
	{
	  yyn += YYTERROR;
	  if (0 <= yyn && yyn <= YYLAST && yycheck[yyn] == YYTERROR)
	    {
	      yyn = yytable[yyn];
	      if (0 < yyn)
		break;
	    }
	}

      /* Pop the current state because it cannot handle the error token.  */
      if (yyssp == yyss)
	YYABORT;


      yydestruct ("Error: popping",
		  yystos[yystate], yyvsp);
      YYPOPSTACK (1);
      yystate = *yyssp;
      YY_STACK_PRINT (yyss, yyssp);
    }

  if (yyn == YYFINAL)
    YYACCEPT;

  *++yyvsp = yylval;


  /* Shift the error token.  */
  YY_SYMBOL_PRINT ("Shifting", yystos[yyn], yyvsp, yylsp);

  yystate = yyn;
  goto yynewstate;


/*-------------------------------------.
| yyacceptlab -- YYACCEPT comes here.  |
`-------------------------------------*/
yyacceptlab:
  yyresult = 0;
  goto yyreturn;

/*-----------------------------------.
| yyabortlab -- YYABORT comes here.  |
`-----------------------------------*/
yyabortlab:
  yyresult = 1;
  goto yyreturn;

#ifndef yyoverflow
/*-------------------------------------------------.
| yyexhaustedlab -- memory exhaustion comes here.  |
`-------------------------------------------------*/
yyexhaustedlab:
  yyerror (YY_("memory exhausted"));
  yyresult = 2;
  /* Fall through.  */
#endif

yyreturn:
  if (yychar != YYEOF && yychar != YYEMPTY)
     yydestruct ("Cleanup: discarding lookahead",
		 yytoken, &yylval);
  /* Do not reclaim the symbols of the rule which action triggered
     this YYABORT or YYACCEPT.  */
  YYPOPSTACK (yylen);
  YY_STACK_PRINT (yyss, yyssp);
  while (yyssp != yyss)
    {
      yydestruct ("Cleanup: popping",
		  yystos[*yyssp], yyvsp);
      YYPOPSTACK (1);
    }
#ifndef yyoverflow
  if (yyss != yyssa)
    YYSTACK_FREE (yyss);
#endif
#if YYERROR_VERBOSE
  if (yymsg != yymsgbuf)
    YYSTACK_FREE (yymsg);
#endif
  /* Make sure YYID is used.  */
  return YYID (yyresult);
}




extern FILE* yyout;
extern YYSTYPE vhdlscannerYYlval;

void vhdlscannerYYerror(const char* /*str*/)
{
  // fprintf(stderr,"\n<---error at line %d  : [ %s]   in file : %s ---->",s_str.yyLineNr,s_str.qstr.data(),s_str.fileName);
  //  exit(0);
}

void vhdlParse()
{
  vhdlscannerYYparse();
}

struct VhdlContainer*  getVhdlCont()
{
  return &s_str;
}

Entry* getVhdlCompound()
{
  if (lastEntity) return lastEntity;
  if (lastCompound) return lastCompound;
  return NULL;
}

QList<VhdlConfNode>& getVhdlConfiguration() { return  configL; }

static void addCompInst(char *n, char* instName, char* comp,int iLine)
{

  current->spec=VhdlDocGen::INSTANTIATION;
  current->section=Entry::VARIABLE_SEC;
  current->startLine=iLine;
  current->bodyLine=iLine;
  current->type=instName;                       // foo:instname e.g proto or work. proto(ttt)
  current->exception=genLabels.lower();         // |arch|label1:label2...
  current->name=n;                              // foo
  current->args=lastCompound->name;             // architecture name
  current->includeName=comp;                    // component/enity/configuration
  int u=genLabels.find("|",1);
  if (u>0)
  {
    current->write=genLabels.right(genLabels.length()-u);
    current->read=genLabels.left(u);
  }
  //printf  (" \n genlable: [%s]  inst: [%s]  name: [%s] %d\n",n,instName,comp,iLine);

  if (lastCompound)
  {
    current->args=lastCompound->name;
    if (true) // !findInstant(current->type))
    {
      initEntry(current);
      instFiles.append(new Entry(*current));
    }

    Entry *temp=current;  // hold  current pointer  (temp=oldEntry)
    current=new Entry;     // (oldEntry != current)
    delete  temp;
  }
  else
  {
    newEntry();
  }
}

static void pushLabel( QCString &label,QCString & val)
{
  label+="|";
  label+=val;
}

static QCString  popLabel(QCString & q)
{
  QCString u=q;
  int i=q.findRev("|");
  if (i<0) return "";
  q = q.left(i);
  return q;
}

static void addConfigureNode(const char* a,const char*b, bool,bool isLeaf,bool inlineConf)
{
  VhdlConfNode* co=0;
  QCString ent,arch,lab;
  QCString l=genLabels;
  ent=a;
  lab =  VhdlDocGen::parseForConfig(ent,arch);

  if (b)
  {
    ent=b;
    lab=VhdlDocGen::parseForBinding(ent,arch);
  }
  int level=0;

  if(!configL.isEmpty())
  {
    VhdlConfNode* vc=configL.getLast();
    level=vc->level;
    if (levelCounter==0)
      pushLabel(forL,ent);
    else if (level<levelCounter)
    {
      if (!isLeaf)
      {
        pushLabel(forL,ent);
      }
    }
    else if (level>levelCounter)
    {
      forL=popLabel(forL); 
    }
  }
  else
  {
    pushLabel(forL,ent);
  }


  if (inlineConf)
  {
    confName=lastCompound->name;
  }

  //fprintf(stderr,"\n[%s %d %d]\n",forL.data(),levelCounter,level);
  co=new VhdlConfNode(a,b,confName.lower().data(),forL.lower().data(),isLeaf);

  if (inlineConf)
  {
    co->isInlineConf=TRUE;
  }

  configL.append(co);

}// addConfigure

//  ------------------------------------------------------------------------------------------------------------

static bool isFuncProcProced()
{
  if (currP==VhdlDocGen::FUNCTION ||
      currP==VhdlDocGen::PROCEDURE ||
      currP==VhdlDocGen::PROCESS
     )
  {
    return TRUE;
  }
  return FALSE;
}

static void initEntry(Entry *e)
{
  e->fileName = s_str.fileName;
  e->lang=SrcLangExt_VHDL;
  isVhdlDocPending();
  initGroupInfo(e);
}

static void addProto(const char *s1,const char *s2,const char *s3,
    const char *s4,const char *s5,const char *s6)
{
  (void)s5; // avoid unused warning
  static QRegExp reg("[\\s]");
  QCString name=s2;
  QStringList ql=QStringList::split(",",name,FALSE);

  for (uint u=0;u<ql.count();u++)
  {
    Argument *arg=new Argument;
    arg->name=ql[u].utf8();
    if (s3)
    {
      arg->type=s3;
    }
    arg->type+=" ";
    arg->type+=s4;
    if (s6)
    {
      arg->type+=s6;
    }
    if (parse_sec==GEN_SEC && param_sec==0)
    {
      arg->defval="gen!";
    }

    if (parse_sec==PARAM_SEC)
    {
      assert(false);
    }

    arg->defval+=s1;
    arg->attrib="";//s6;

    current->argList->append(arg);
    current->args+=s2;
    current->args+=",";
  }
}

static void createFunction(const QCString &impure,uint64 spec,
    const QCString &fname)
{

  current->spec=spec;
  current->section=Entry::FUNCTION_SEC;

  if (impure=="impure" || impure=="pure")  
  {
    current->exception=impure;
  }

  if (parse_sec==GEN_SEC)
  {
    current->spec= VhdlDocGen::GENERIC;
    current->section=Entry::FUNCTION_SEC;
  }

  if (currP==VhdlDocGen::PROCEDURE)
  {
    current->name=impure;
    current->exception="";
  }
  else
  {
    current->name=fname;
  }

  if (spec==VhdlDocGen::PROCESS)
  {

    current->args=fname;
    current->name=impure;
    VhdlDocGen::deleteAllChars(current->args,' ');
    if (!fname.isEmpty())
    {
      QStringList q1=QStringList::split(",",fname);
      for (uint ii=0;ii<q1.count();ii++)
      {
        Argument *arg=new Argument;
        arg->name=q1[ii].utf8();
        current->argList->append(arg);
      }
    }
    return;
  }

  current->startLine=s_str.iLine;
  current->bodyLine=s_str.iLine;

}

static void addVhdlType(const QCString &name,int startLine,int section,
    uint64 spec,const char* args,const char* type,Protection prot)
{
  static QRegExp reg("[\\s]");

  if (isFuncProcProced() || VhdlDocGen::getFlowMember())   return;
    
  if (parse_sec==GEN_SEC)
  {
    spec= VhdlDocGen::GENERIC;
  }

  // more than one name   ?
  QStringList ql=QStringList::split(",",name,FALSE);

  for (uint u=0;u<ql.count();u++)
  {
    current->name=ql[u].utf8();
  

    current->startLine=startLine;
    current->bodyLine=startLine;
    current->section=section;
    current->spec=spec;
    current->fileName=s_str.fileName;
    if (current->args.isEmpty())
    {
      current->args=args;
//    current->args.replace(reg,"%"); // insert dummy chars because wihte spaces are removed
    }
    current->type=type;
//  current->type.replace(reg,"%"); // insert dummy chars because white spaces are removed
    current->protection=prot;
 
       if (!lastCompound && (section==Entry::VARIABLE_SEC) &&  (spec == VhdlDocGen::USE || spec == VhdlDocGen::LIBRARY) )
       {
         libUse.append(new Entry(*current));
         current->reset();
       }
    newEntry();
  }
}

static void newEntry()
{

  if (VhdlDocGen::isVhdlClass(current))
  {
    current_root->addSubEntry(current);
  }
  else
  {
    if (lastCompound)
    {
      lastCompound->addSubEntry(current);
    }
    else
    {
      if (lastEntity)
      {
        lastEntity->addSubEntry(current);
      }
      else
      {
        current_root->addSubEntry(current);
      }
    }
  }
  current = new Entry ;
  initEntry(current);
}

void createFlow()
{
  if (!VhdlDocGen::getFlowMember()) 
  {
    return;
  }
  QCString q,ret;

  if (currP==VhdlDocGen::FUNCTION)
  {
    q=":function( ";
    FlowChart::alignFuncProc(q,tempEntry->argList,true);
    q+=")";
  }
  else if (currP==VhdlDocGen::PROCEDURE)
  {
    q=":procedure (";    
    FlowChart::alignFuncProc(q,tempEntry->argList,false);
    q+=")";
  }
  else  
  {  
    q=":process( "+tempEntry->args;
    q+=")";
  }

  q.prepend(VhdlDocGen::getFlowMember()->name().data());

  FlowChart::addFlowChart(FlowChart::START_NO,q,0);

  if (currP==VhdlDocGen::FUNCTION)
  {
    ret="end function ";  
  }
  else if (currP==VhdlDocGen::PROCEDURE)
  {
    ret="end procedure";
  }
  else 
  {
    ret="end process ";
  }

  FlowChart::addFlowChart(FlowChart::END_NO,ret,0);
  //  FlowChart::printFlowList();
  FlowChart::writeFlowChart();  
  currP=0;
}


