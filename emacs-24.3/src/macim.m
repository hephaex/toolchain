/* Implementation of Input Method Extension for MacOS X.
   Copyright (C) 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011
   Taiichi Hashimoto <taiichi2@mac.com>.
*/

#include "config.h"

#ifdef NS_IMPL_COCOA

#include <math.h>
#include <sys/types.h>
#include <time.h>
#include <signal.h>
#include <unistd.h>

#include <Carbon/Carbon.h>

#include "lisp.h"
#include "blockinput.h"

enum output_method
{
  output_initial,
  output_termcap,
  output_x_window,
  output_msdos_raw,
  output_w32,
  output_mac,
  output_ns
} output_method;

#include "termhooks.h"
#include "keyboard.h"
#include "buffer.h"

//extern Lisp_Object Qcurrent_input_method;
//extern int cursor_in_echo_area;
static Lisp_Object Qmac_keys_passed_to_system;

void mac_init_input_method ();
int mac_pass_key_to_system (int code, int modifiers);
int mac_pass_key_directly_to_emacs ();
int mac_store_change_input_method_event ();

DEFUN ("mac-input-source-is-ascii-capable", Fmac_input_source_is_ascii_capable, Smac_input_source_is_ascii_capable,
       0, 0, 0,
       doc: /* Is current input source ascii capable? */)
     (void)
{
  TISInputSourceRef is = TISCopyCurrentKeyboardInputSource();
  CFBooleanRef ret = TISGetInputSourceProperty(is, kTISPropertyInputSourceIsASCIICapable);
  
  return CFBooleanGetValue(ret)? Qt : Qnil;
}

DEFUN ("mac-get-input-source-list", Fmac_get_input_source_list, Smac_get_input_source_list,
       0, 0, 0,
       doc: /* get input source list on MacOSX */)
     (void)
{
  NSArray *is_list = (NSArray *)TISCreateInputSourceList(NULL, false);
  int list_size = [is_list count];
  Lisp_Object list[list_size];
  int i;

  for (i = 0; i < list_size; i++) {
    TISInputSourceRef is = (TISInputSourceRef)[is_list objectAtIndex:i];
    NSString *id = (NSString *)TISGetInputSourceProperty(is, kTISPropertyInputSourceID);
    list[i] = make_string([id UTF8String],
			  [id lengthOfBytesUsingEncoding:NSUTF8StringEncoding]);
  }

  return Flist(list_size, list);
}

DEFUN ("mac-get-current-input-source", Fmac_get_current_input_source, Smac_get_current_input_source,
       0, 0, 0,
       doc: /* get current input source on MacOSX */)
     (void)
{
  TISInputSourceRef is = TISCopyCurrentKeyboardInputSource();
  NSString *id = (NSString *)TISGetInputSourceProperty(is, kTISPropertyInputSourceID);

  return make_string([id UTF8String],
		     [id lengthOfBytesUsingEncoding:NSUTF8StringEncoding]);
}

DEFUN ("mac-toggle-input-source", Fmac_toggle_input_source, Smac_toggle_input_source,
       1, 1, 0,
       doc: /* toggle input source on MacOSX */)
     (arg)
     Lisp_Object arg;
{
  TISInputSourceRef is = NULL;

  if (NILP (arg))
    {
      is = TISCopyCurrentASCIICapableKeyboardInputSource();
    }
  else
    {
      NSString *locale;
      NSArray *languages = [NSLocale preferredLanguages];
      if (languages != nil) {
	  locale = [languages objectAtIndex:0];
      } else {
	  locale = [[NSLocale currentLocale]
		  	objectForKey:NSLocaleLanguageCode];
      }
      is = TISCopyInputSourceForLanguage((CFStringRef)locale);
    }
  if (is) TISSelectInputSource(is);

  return arg;
}

int
mac_store_change_input_method_event ()
{
  Lisp_Object dim;
  int ret = FALSE;
  
  dim = Fsymbol_value (intern ("default-input-method"));
  if (STRINGP (dim) && strcmp(SDATA (dim), "MacOSX") == 0)
    {
      ret = TRUE;
    }

  return ret;
}

int
mac_pass_key_to_system (int code, int modifiers)
{
  Lisp_Object keys = Fsymbol_value (Qmac_keys_passed_to_system);
  Lisp_Object m, k; 

  while (!NILP (keys))
    {
      m = XCAR (XCAR (keys));
      k = XCDR (XCAR (keys));
      keys = XCDR (keys);

      if (NUMBERP (m) && modifiers == XINT (m))
	if (NILP (k)
	    || (NUMBERP (k) && code == XINT (k)))
	  return TRUE;
    }
  
  return FALSE;
}

int
mac_pass_key_directly_to_emacs (void)
{

  if (NILP (Fmac_input_source_is_ascii_capable()))
    {
      if (NILP (Vmac_use_input_method_on_system)
	  || this_command_key_count 
	  || cursor_in_echo_area 
	  || !NILP (BVAR (current_buffer, read_only)))
	return TRUE;
    }

  return FALSE;
}


void mac_init_input_method (void)
{
  Qmac_keys_passed_to_system = intern ("mac-keys-passed-to-system");
  staticpro (&Qmac_keys_passed_to_system);

  DEFVAR_LISP ("mac-use-input-method-on-system", Vmac_use_input_method_on_system,
               doc: /* If it is non-nil, use input method on system. */);
  Vmac_use_input_method_on_system = Qt;

  defsubr (&Smac_input_source_is_ascii_capable);
  defsubr (&Smac_get_input_source_list);
  defsubr (&Smac_get_current_input_source);
  defsubr (&Smac_toggle_input_source);
}
#endif
