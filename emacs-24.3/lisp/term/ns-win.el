;;; ns-win.el --- lisp side of interface with NeXT/Open/GNUstep/MacOS X window system  -*- lexical-binding: t -*-

;; Copyright (C) 1993-1994, 2005-2013 Free Software Foundation, Inc.

;; Authors: Carl Edman
;;	Christian Limpach
;;	Scott Bender
;;	Christophe de Dinechin
;;	Adrian Robert
;; Keywords: terminals

;; This file is part of GNU Emacs.

;; GNU Emacs is free software: you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; GNU Emacs is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with GNU Emacs.  If not, see <http://www.gnu.org/licenses/>.

;;; Commentary:

;; ns-win.el: this file is loaded from ../lisp/startup.el when it
;; recognizes that Nextstep windows are to be used.  Command line
;; switches are parsed and those pertaining to Nextstep are processed
;; and removed from the command line.  The Nextstep display is opened
;; and hooks are set for popping up the initial window.

;; startup.el will then examine startup files, and eventually call the hooks
;; which create the first window (s).

;; A number of other Nextstep convenience functions are defined in
;; this file, which works in close coordination with src/nsfns.m.

;;; Code:
(eval-when-compile (require 'cl-lib))
(or (featurep 'ns)
    (error "%s: Loading ns-win.el but not compiled for GNUstep/MacOS"
           (invocation-name)))

;; Documentation-purposes only: actually loaded in loadup.el.
(require 'frame)
(require 'mouse)
(require 'faces)
(require 'menu-bar)
(require 'fontset)

(defgroup ns nil
  "GNUstep/Mac OS X specific features."
  :group 'environment)

;;;; Command line argument handling.

(defvar x-invocation-args)
(defvar ns-command-line-resources nil)  ; FIXME unused?

;; nsterm.m.
(defvar ns-input-file)

(defun ns-handle-nxopen (_switch &optional temp)
  (setq unread-command-events (append unread-command-events
                                      (if temp '(ns-open-temp-file)
                                        '(ns-open-file)))
        ns-input-file (append ns-input-file (list (pop x-invocation-args)))))

(defun ns-handle-nxopentemp (switch)
  (ns-handle-nxopen switch t))

(defun ns-ignore-1-arg (_switch)
  (setq x-invocation-args (cdr x-invocation-args)))

(defun ns-parse-geometry (geom)
  "Parse a Nextstep-style geometry string GEOM.
Returns an alist of the form ((top . TOP), (left . LEFT) ... ).
The properties returned may include `top', `left', `height', and `width'."
  (when (string-match "\\([0-9]+\\)\\( \\([0-9]+\\)\\( \\([0-9]+\\)\
\\( \\([0-9]+\\) ?\\)?\\)?\\)?"
		      geom)
    (apply
     'append
     (list
      (list (cons 'top (string-to-number (match-string 1 geom))))
      (if (match-string 3 geom)
	  (list (cons 'left (string-to-number (match-string 3 geom)))))
      (if (match-string 5 geom)
	  (list (cons 'height (string-to-number (match-string 5 geom)))))
      (if (match-string 7 geom)
	  (list (cons 'width (string-to-number (match-string 7 geom)))))))))

;;;; Keyboard mapping.

(define-obsolete-variable-alias 'ns-alternatives-map 'x-alternatives-map "24.1")

;; Here are some Nextstep-like bindings for command key sequences.
(define-key global-map [?\s-,] 'customize)
(define-key global-map [?\s-'] 'next-multiframe-window)
(define-key global-map [?\s-`] 'other-frame)
(define-key global-map [?\s-~] 'ns-prev-frame)
(define-key global-map [?\s--] 'center-line)
(define-key global-map [?\s-:] 'ispell)
(define-key global-map [?\s-\;] 'ispell-next)
(define-key global-map [?\s-?] 'info)
(define-key global-map [?\s-^] 'kill-some-buffers)
(define-key global-map [?\s-&] 'kill-this-buffer)
(define-key global-map [?\s-C] 'ns-popup-color-panel)
(define-key global-map [?\s-D] 'dired)
(define-key global-map [?\s-E] 'edit-abbrevs)
(define-key global-map [?\s-L] 'shell-command)
(define-key global-map [?\s-M] 'manual-entry)
(define-key global-map [?\s-S] 'ns-write-file-using-panel)
(define-key global-map [?\s-a] 'mark-whole-buffer)
(define-key global-map [?\s-c] 'ns-copy-including-secondary)
(define-key global-map [?\s-d] 'isearch-repeat-backward)
(define-key global-map [?\s-e] 'isearch-yank-kill)
(define-key global-map [?\s-f] 'isearch-forward)
(define-key global-map [?\s-g] 'isearch-repeat-forward)
(define-key global-map [?\s-h] 'ns-do-hide-emacs)
(define-key global-map [?\s-H] 'ns-do-hide-others)
(define-key global-map [?\s-j] 'exchange-point-and-mark)
(define-key global-map [?\s-k] 'kill-this-buffer)
(define-key global-map [?\s-l] 'goto-line)
(define-key global-map [?\s-m] 'iconify-frame)
(define-key global-map [?\s-n] 'make-frame)
(define-key global-map [?\s-o] 'ns-open-file-using-panel)
(define-key global-map [?\s-p] 'ns-print-buffer)
(define-key global-map [?\s-q] 'save-buffers-kill-emacs)
(define-key global-map [?\s-s] 'save-buffer)
(define-key global-map [?\s-t] 'ns-popup-font-panel)
(define-key global-map [?\s-u] 'revert-buffer)
(define-key global-map [?\s-v] 'yank)
(define-key global-map [?\s-w] 'delete-frame)
(define-key global-map [?\s-x] 'kill-region)
(define-key global-map [?\s-y] 'ns-paste-secondary)
(define-key global-map [?\s-z] 'undo)
(define-key global-map [?\s-|] 'shell-command-on-region)
(define-key global-map [s-kp-bar] 'shell-command-on-region)
;; (as in Terminal.app)
(define-key global-map [s-right] 'ns-next-frame)
(define-key global-map [s-left] 'ns-prev-frame)

(define-key global-map [home] 'beginning-of-buffer)
(define-key global-map [end] 'end-of-buffer)
(define-key global-map [kp-home] 'beginning-of-buffer)
(define-key global-map [kp-end] 'end-of-buffer)
(define-key global-map [kp-prior] 'scroll-down-command)
(define-key global-map [kp-next] 'scroll-up-command)

;; Allow shift-clicks to work similarly to under Nextstep.
(define-key global-map [S-mouse-1] 'mouse-save-then-kill)
(global-unset-key [S-down-mouse-1])

;; Special Nextstep-generated events are converted to function keys.  Here
;; are the bindings for them.  Note, these keys are actually declared in
;; x-setup-function-keys in common-win.
(define-key global-map [ns-power-off] 'save-buffers-kill-emacs)
(define-key global-map [ns-open-file] 'ns-find-file)
(define-key global-map [ns-open-temp-file] [ns-open-file])
(define-key global-map [ns-drag-file] 'ns-find-file)
(define-key global-map [ns-drag-color] 'ns-set-foreground-at-mouse)
(define-key global-map [S-ns-drag-color] 'ns-set-background-at-mouse)
(define-key global-map [ns-drag-text] 'ns-insert-text)
(define-key global-map [ns-change-font] 'ns-respond-to-change-font)
(define-key global-map [ns-open-file-line] 'ns-open-file-select-line)
(define-key global-map [ns-spi-service-call] 'ns-spi-service-call)
(define-key global-map [ns-new-frame] 'make-frame)
(define-key global-map [ns-toggle-toolbar] 'ns-toggle-toolbar)
(define-key global-map [ns-show-prefs] 'customize)
(define-key global-map [mac-change-input-method] 'mac-change-input-method)


;; Set up a number of aliases and other layers to pretend we're using
;; the Choi/Mitsuharu Carbon port.

(defvaralias 'mac-allow-anti-aliasing 'ns-antialias-text)
(defvaralias 'mac-command-modifier 'ns-command-modifier)
(defvaralias 'mac-right-command-modifier 'ns-right-command-modifier)
(defvaralias 'mac-control-modifier 'ns-control-modifier)
(defvaralias 'mac-right-control-modifier 'ns-right-control-modifier)
(defvaralias 'mac-option-modifier 'ns-option-modifier)
(defvaralias 'mac-right-option-modifier 'ns-right-option-modifier)
(defvaralias 'mac-function-modifier 'ns-function-modifier)
(declare-function ns-do-applescript "nsfns.m" (script))
(defalias 'do-applescript 'ns-do-applescript)

;;;; Services
(declare-function ns-perform-service "nsfns.m" (service send))

(defun ns-define-service (path)
  (let ((mapping [menu-bar services])
	(service (mapconcat 'identity path "/"))
	(name (intern
               (subst-char-in-string
                ?\s ?-
                (mapconcat 'identity (cons "ns-service" path) "-")))))
    ;; This defines the function.
    (defalias name
      (lambda (arg)
        (interactive "p")
        (let* ((in-string
                (cond ((stringp arg) arg)
                      (mark-active
                       (buffer-substring (region-beginning) (region-end)))))
               (out-string (ns-perform-service service in-string)))
          (cond
           ((stringp arg) out-string)
           ((and out-string (or (not in-string)
                                (not (string= in-string out-string))))
            (if mark-active (delete-region (region-beginning) (region-end)))
            (insert out-string)
            (setq deactivate-mark nil))))))
    (cond
     ((lookup-key global-map mapping)
      (while (cdr path)
	(setq mapping (vconcat mapping (list (intern (car path)))))
	(if (not (keymapp (lookup-key global-map mapping)))
	    (define-key global-map mapping
	      (cons (car path) (make-sparse-keymap (car path)))))
	(setq path (cdr path)))
      (setq mapping (vconcat mapping (list (intern (car path)))))
      (define-key global-map mapping (cons (car path) name))))
    name))

;; nsterm.m
(defvar ns-input-spi-name)
(defvar ns-input-spi-arg)

(declare-function dnd-open-file "dnd" (uri action))

(defun ns-spi-service-call ()
  "Respond to a service request."
  (interactive)
  (cond ((string-equal ns-input-spi-name "open-selection")
	 (switch-to-buffer (generate-new-buffer "*untitled*"))
	 (insert ns-input-spi-arg))
	((string-equal ns-input-spi-name "open-file")
	 (dnd-open-file ns-input-spi-arg nil))
	((string-equal ns-input-spi-name "mail-selection")
	 (compose-mail)
	 (rfc822-goto-eoh)
	 (forward-line 1)
	 (insert ns-input-spi-arg))
	((string-equal ns-input-spi-name "mail-to")
	 (compose-mail ns-input-spi-arg))
	(t (error (concat "Service " ns-input-spi-name " not recognized")))))


;; Composed key sequence handling for Nextstep system input methods.
;; (On Nextstep systems, input methods are provided for CJK
;; characters, etc. which require multiple keystrokes, and during
;; entry a partial ("working") result is typically shown in the
;; editing window.)

(defface ns-working-text-face
  '((((background dark)) :underline "gray80")
    (t :underline "gray20"))
  "Face used to highlight working text during compose sequence insert."
  :group 'ns)

(defface ns-marked-text-face
  '((((background dark)) :underline "gray80")
    (t :underline "gray20"))
  "Face used to highlight marked text during compose sequence insert."
  :group 'ns)

(defface ns-unmarked-text-face
  '((((background dark)) :underline "gray20")
    (t :underline "gray80"))
  "Face used to highlight marked text during compose sequence insert."
  :group 'ns)

(defvar ns-working-overlay nil
  "Overlay used to highlight working text during compose sequence insert.
When text is in th echo area, this just stores the length of the working text.")

(defvar ns-marked-overlay nil
  "Overlay used to highlight marked text during compose sequence insert.")

(defvar ns-working-text)		; nsterm.m

;; Test if in echo area, based on mac-win.el 2007/08/26 unicode-2.
;; This will fail if called from a NONASCII_KEYSTROKE event on the global map.
(defun ns-in-echo-area ()
  "Whether, for purposes of inserting working composition text, the minibuffer
is currently being used."
  (setq mac-in-echo-area 
	(or isearch-mode
	    (and cursor-in-echo-area (current-message))
	    ;; Overlay strings are not shown in some cases.
	    (get-char-property (point) 'invisible)
	    (and (not (bobp))
		 (or (and (get-char-property (point) 'display)
			  (eq (get-char-property (1- (point)) 'display)
			      (get-char-property (point) 'display)))
		     (and (get-char-property (point) 'composition)
			  (eq (get-char-property (1- (point)) 'composition)
			      (get-char-property (point) 'composition)))))))
  mac-in-echo-area)

;; The 'interactive' here stays for subinvocations, so the ns-in-echo-area
;; always returns nil for some reason.  If this WASN'T the case, we could
;; map this to [ns-insert-working-text] and eliminate Fevals in nsterm.m.
;; These functions test whether in echo area and delegate accordingly.
(defun ns-put-working-text ()
  (interactive)
  (if (ns-in-echo-area) (ns-echo-working-text) (ns-insert-working-text)))

(defun ns-unput-working-text ()
  (interactive)
  (ns-delete-working-text))

(defun ns-insert-working-text ()
  "Insert contents of `ns-working-text' as UTF-8 string and mark with
`ns-working-overlay'.  Any previously existing working text is cleared first.
The overlay is assigned the face `ns-working-text-face'."
  ;; FIXME: if buffer is read-only, don't try to insert anything
  ;;  and if text is bound to a command, execute that instead (Bug#1453)
  (interactive)
  (ns-delete-working-text)
  (let ((start (point)))
    (insert ns-working-text)
    (overlay-put (setq ns-working-overlay (make-overlay start (point)
							(current-buffer) nil t))
		 'face 'ns-working-text-face)))

(defun ns-echo-working-text ()
  "Echo contents of `ns-working-text' in message display area.
See `ns-insert-working-text'."
  (let* ((msg (current-message))
         (msglen (length msg))
         message-log-max)
    (if (integerp ns-working-overlay)
	(progn
	  (setq msg (substring msg 0 (- (length msg) ns-working-overlay)))
	  (setq msglen (length msg))))
    (setq ns-working-overlay (length ns-working-text))
    (setq msg (concat msg ns-working-text))
    (put-text-property msglen (+ msglen ns-working-overlay)
			'face 'ns-working-text-face msg)
     (message "%s" msg)))

(defun ns-put-marked-text (event)
  (interactive "e")

  (let ((pos (nth 1 event))
	(len (nth 2 event)))
    (if (ns-in-echo-area)
	(ns-echo-marked-text pos len)
      (ns-insert-marked-text pos len))))

(defun ns-insert-marked-text (pos len)
  "Insert contents of `ns-working-text' as UTF-8 string and mark with
`ns-working-overlay' and `ns-marked-overlay'.  Any previously existing
working text is cleared first. The overlay is assigned the faces 
`ns-working-text-face' and `ns-marked-text-face'."
  (ns-delete-working-text)
  (let ((start (point)))
    (if (<= pos (length ns-working-text))
      (progn
	(put-text-property pos len 'face 'ns-working-text-face ns-working-text)
	(insert ns-working-text)
	(if (= len 0)
	    (overlay-put (setq ns-working-overlay
			       (make-overlay start (point) (current-buffer) nil t))
			 'face 'ns-working-text-face)
	  (overlay-put (setq ns-working-overlay
			     (make-overlay start (point) (current-buffer) nil t))
		       'face 'ns-unmarked-text-face)
	  (overlay-put (setq ns-marked-overlay 
			     (make-overlay (+ start pos) (+ start pos len)
					   (current-buffer) nil t))
		       'face 'ns-marked-text-face))
	(goto-char (+ start pos))))))
    
(defun ns-echo-marked-text (pos len)
  "Echo contents of `ns-working-text' in message display area.
See `ns-insert-working-text'."
  (let* ((msg (current-message))
         (msglen (length msg))
         message-log-max)
    (when (integerp ns-working-overlay)
      (setq msg (substring msg 0 (- (length msg) ns-working-overlay)))
      (setq msglen (length msg)))
    (setq ns-working-overlay (length ns-working-text))
    (setq msg (concat msg ns-working-text))
    (if (= len 0)
        (put-text-property msglen (+ msglen ns-working-overlay)
                           'face 'ns-working-text-face msg)
      (put-text-property msglen (+ msglen ns-working-overlay)
                         'face 'ns-unmarked-text-face msg)
      (put-text-property (+ msglen pos) (+ msglen pos len)
                         'face 'ns-marked-text-face msg))
    (message "%s" msg)))

(defun ns-delete-working-text()
  "Delete working text and clear `ns-working-overlay' and `ns-marked-overlay'."
  (interactive)
  (when (and (overlayp ns-marked-overlay)
	     ;; Still alive
	     (overlay-buffer ns-marked-overlay))
    (with-current-buffer (overlay-buffer ns-marked-overlay)
      (delete-overlay ns-marked-overlay)))
  (setq ns-marked-overlay nil)
  (cond
   ((and (overlayp ns-working-overlay)
         ;; Still alive?
         (overlay-buffer ns-working-overlay))
    (with-current-buffer (overlay-buffer ns-working-overlay)
      (delete-region (overlay-start ns-working-overlay)
                     (overlay-end ns-working-overlay))
      (delete-overlay ns-working-overlay)))
   ((integerp ns-working-overlay)
    (let ((msg (current-message))
          message-log-max)
      (setq msg (substring msg 0 (- (length msg) ns-working-overlay)))
      (message "%s" msg))))
  (setq ns-working-overlay nil))


(declare-function ns-convert-utf8-nfd-to-nfc "nsfns.m" (str))

;;;; OS X file system Unicode UTF-8 NFD (decomposed form) support
;; Lisp code based on utf-8m.el, by Seiji Zenitani, Eiji Honjoh, and
;; Carsten Bormann.
(when (eq system-type 'darwin)
  (defun ns-utf8-nfd-post-read-conversion (length)
    "Calls `ns-convert-utf8-nfd-to-nfc' to compose char sequences."
    (save-excursion
      (save-restriction
        (narrow-to-region (point) (+ (point) length))
        (let ((str (buffer-string)))
          (delete-region (point-min) (point-max))
          (insert (ns-convert-utf8-nfd-to-nfc str))
          (- (point-max) (point-min))))))

  (define-coding-system 'utf-8-nfd
    "UTF-8 NFD (decomposed) encoding."
    :coding-type 'utf-8
    :mnemonic ?U
    :charset-list '(unicode)
    :post-read-conversion 'ns-utf8-nfd-post-read-conversion)
  (set-file-name-coding-system 'utf-8-nfd))

;;;; Inter-app communications support.

(defvar ns-input-text)			; nsterm.m

(defun ns-insert-text ()
  "Insert contents of `ns-input-text' at point."
  (interactive)
  (insert ns-input-text)
  (setq ns-input-text nil))

(defun ns-insert-file ()
  "Insert contents of file `ns-input-file' like insert-file but with less
prompting.  If file is a directory perform a `find-file' on it."
  (interactive)
  (let ((f (pop ns-input-file)))
    (if (file-directory-p f)
        (find-file f)
      (push-mark (+ (point) (cadr (insert-file-contents f)))))))

(defvar ns-select-overlay nil
  "Overlay used to highlight areas in files requested by Nextstep apps.")
(make-variable-buffer-local 'ns-select-overlay)

(defvar ns-input-line) 			; nsterm.m

(defun ns-open-file-select-line ()
  "Open a buffer containing the file `ns-input-file'.
Lines are highlighted according to `ns-input-line'."
  (interactive)
  (ns-find-file)
  (cond
   ((and ns-input-line (buffer-modified-p))
    (if ns-select-overlay
        (setq ns-select-overlay (delete-overlay ns-select-overlay)))
    (deactivate-mark)
    (goto-char (point-min))
    (forward-line (1- (if (consp ns-input-line)
                          (min (car ns-input-line) (cdr ns-input-line))
                        ns-input-line))))
   (ns-input-line
    (if (not ns-select-overlay)
        (overlay-put (setq ns-select-overlay (make-overlay (point-min)
                                                           (point-min)))
                     'face 'highlight))
    (let ((beg (save-excursion
                 (goto-char (point-min))
                 (line-beginning-position
                  (if (consp ns-input-line)
                      (min (car ns-input-line) (cdr ns-input-line))
                    ns-input-line))))
          (end (save-excursion
                 (goto-char (point-min))
                 (line-beginning-position
                  (1+ (if (consp ns-input-line)
                          (max (car ns-input-line) (cdr ns-input-line))
                        ns-input-line))))))
      (move-overlay ns-select-overlay beg end)
      (deactivate-mark)
      (goto-char beg)))
   (t
    (if ns-select-overlay
        (setq ns-select-overlay (delete-overlay ns-select-overlay))))))

(defun ns-unselect-line ()
  "Removes any Nextstep highlight a buffer may contain."
  (if ns-select-overlay
      (setq ns-select-overlay (delete-overlay ns-select-overlay))))

(add-hook 'first-change-hook 'ns-unselect-line)

;;;; Preferences handling.
(declare-function ns-get-resource "nsfns.m" (owner name))

(defun get-lisp-resource (arg1 arg2)
  (let ((res (ns-get-resource arg1 arg2)))
    (cond
     ((not res) 'unbound)
     ((string-equal (upcase res) "YES") t)
     ((string-equal (upcase res) "NO")  nil)
     (t (read res)))))

;; nsterm.m

(declare-function ns-read-file-name "nsfns.m"
		  (prompt &optional dir mustmatch init dir_only_p))

;;;; File handling.

(defun x-file-dialog (prompt dir default_filename mustmatch only_dir_p)
"Read file name, prompting with PROMPT in directory DIR.
Use a file selection dialog.  Select DEFAULT-FILENAME in the dialog's file
selection box, if specified.  If MUSTMATCH is non-nil, the returned file
or directory must exist.

This function is only defined on NS, MS Windows, and X Windows with the
Motif or Gtk toolkits.  With the Motif toolkit, ONLY-DIR-P is ignored.
Otherwise, if ONLY-DIR-P is non-nil, the user can only select directories."
  (ns-read-file-name prompt dir mustmatch default_filename only_dir_p))

(defun ns-open-file-using-panel ()
  "Pop up open-file panel, and load the result in a buffer."
  (interactive)
  ;; Prompt dir defaultName isLoad initial.
  (setq ns-input-file (ns-read-file-name "Select File to Load" nil t nil))
  (if ns-input-file
      (and (setq ns-input-file (list ns-input-file)) (ns-find-file))))

(defun ns-write-file-using-panel ()
  "Pop up save-file panel, and save buffer in resulting name."
  (interactive)
  (let (ns-output-file)
    ;; Prompt dir defaultName isLoad initial.
    (setq ns-output-file (ns-read-file-name "Save As" nil nil nil))
    (message ns-output-file)
    (if ns-output-file (write-file ns-output-file))))

(defcustom ns-pop-up-frames 'fresh
  "Non-nil means open files upon request from the Workspace in a new frame.
If t, always do so.  Any other non-nil value means open a new frame
unless the current buffer is a scratch buffer."
  :type '(choice (const :tag "Never" nil)
                 (const :tag "Always" t)
                 (other :tag "Except for scratch buffer" fresh))
  :version "23.1"
  :group 'ns)

(declare-function ns-hide-emacs "nsfns.m" (on))

(defun ns-find-file ()
  "Do a `find-file' with the `ns-input-file' as argument."
  (interactive)
  (let* ((f (file-truename
	     (expand-file-name (pop ns-input-file)
			       command-line-default-directory)))
         (file (find-file-noselect f))
         (bufwin1 (get-buffer-window file 'visible))
         (bufwin2 (get-buffer-window "*scratch*" 'visible)))
    (cond
     (bufwin1
      (select-frame (window-frame bufwin1))
      (raise-frame (window-frame bufwin1))
      (select-window bufwin1))
     ((and (eq ns-pop-up-frames 'fresh) bufwin2)
      (ns-hide-emacs 'activate)
      (select-frame (window-frame bufwin2))
      (raise-frame (window-frame bufwin2))
      (select-window bufwin2)
      (find-file f))
     (ns-pop-up-frames
      (ns-hide-emacs 'activate)
      (let ((pop-up-frames t)) (pop-to-buffer file nil)))
     (t
      (ns-hide-emacs 'activate)
      (find-file f)))))

;;;; Frame-related functions.

;; nsterm.m
(defvar ns-alternate-modifier)
(defvar ns-right-alternate-modifier)
(defvar ns-right-command-modifier)
(defvar ns-right-control-modifier)

;; You say tomAYto, I say tomAHto..
(defvaralias 'ns-option-modifier 'ns-alternate-modifier)
(defvaralias 'ns-right-option-modifier 'ns-right-alternate-modifier)

(defun ns-do-hide-emacs ()
  (interactive)
  (ns-hide-emacs t))

(declare-function ns-hide-others "nsfns.m" ())

(defun ns-do-hide-others ()
  (interactive)
  (ns-hide-others))

(declare-function ns-emacs-info-panel "nsfns.m" ())

(defun ns-do-emacs-info-panel ()
  (interactive)
  (ns-emacs-info-panel))

(defun ns-next-frame ()
  "Switch to next visible frame."
  (interactive)
  (other-frame 1))

(defun ns-prev-frame ()
  "Switch to previous visible frame."
  (interactive)
  (other-frame -1))

;; If no position specified, make new frame offset by 25 from current.
(defvar parameters)		     ; dynamically bound in make-frame
(add-hook 'before-make-frame-hook
          (lambda ()
            (let ((left (cdr (assq 'left (frame-parameters))))
                  (top (cdr (assq 'top (frame-parameters)))))
              (if (consp left) (setq left (cadr left)))
              (if (consp top) (setq top (cadr top)))
              (cond
               ((or (assq 'top parameters) (assq 'left parameters)))
               ((or (not left) (not top)))
               (t
                (setq parameters (cons (cons 'left (+ left 25))
                                       (cons (cons 'top (+ top 25))
                                             parameters))))))))

;; frame will be focused anyway, so select it
;; (if this is not done, mode line is dimmed until first interaction)
(add-hook 'after-make-frame-functions 'select-frame)

(defvar tool-bar-mode)
(declare-function tool-bar-mode "tool-bar" (&optional arg))

;; Based on a function by David Reitter <dreitter@inf.ed.ac.uk> ;
;; see http://lists.gnu.org/archive/html/emacs-devel/2005-09/msg00681.html .
(defun ns-toggle-toolbar (&optional frame)
  "Switches the tool bar on and off in frame FRAME.
 If FRAME is nil, the change applies to the selected frame."
  (interactive)
  (modify-frame-parameters
   frame (list (cons 'tool-bar-lines
		       (if (> (or (frame-parameter frame 'tool-bar-lines) 0) 0)
				   0 1)) ))
  (if (not tool-bar-mode) (tool-bar-mode t)))


;;;; Dialog-related functions.

;; Ask user for confirm before printing.  Due to Kevin Rodgers.
(defun ns-print-buffer ()
  "Interactive front-end to `print-buffer': asks for user confirmation first."
  (interactive)
  (if (and (called-interactively-p 'interactive)
           (or (listp last-nonmenu-event)
               (and (char-or-string-p (event-basic-type last-command-event))
                    (memq 'super (event-modifiers last-command-event)))))
      (let ((last-nonmenu-event (if (listp last-nonmenu-event)
                                    last-nonmenu-event
                                  ;; Fake it:
                                  `(mouse-1 POSITION 1))))
        (if (y-or-n-p (format "Print buffer %s? " (buffer-name)))
            (print-buffer)
	  (error "Cancelled")))
    (print-buffer)))

;;;; Font support.

;; Needed for font listing functions under both backend and normal
(setq scalable-fonts-allowed t)

;; Set to use font panel instead
(declare-function ns-popup-font-panel "nsfns.m" (&optional frame))
(defalias 'x-select-font 'ns-popup-font-panel "Pop up the font panel.
This function has been overloaded in Nextstep.")
(defalias 'mouse-set-font 'ns-popup-font-panel "Pop up the font panel.
This function has been overloaded in Nextstep.")

;; nsterm.m
(defvar ns-input-font)
(defvar ns-input-fontsize)

(defun ns-respond-to-change-font ()
  "Respond to changeFont: event, expecting `ns-input-font' and\n\
`ns-input-fontsize' of new font."
  (interactive)
  (modify-frame-parameters (selected-frame)
                           (list (cons 'fontsize ns-input-fontsize)))
  (modify-frame-parameters (selected-frame)
                           (list (cons 'font ns-input-font)))
  (set-frame-font ns-input-font))


;; Default fontset for Mac OS X.  This is mainly here to show how a fontset
;; can be set up manually.  Ordinarily, fontsets are auto-created whenever
;; a font is chosen by
(defvar ns-standard-fontset-spec
  ;; Only some code supports this so far, so use uglier XLFD version
  ;; "-ns-*-*-*-*-*-10-*-*-*-*-*-fontset-standard,latin:Courier,han:Kai"
  (mapconcat 'identity
             '("-ns-*-*-*-*-*-10-*-*-*-*-*-fontset-standard"
               "latin:-*-Courier-*-*-*-*-10-*-*-*-*-*-iso10646-1"
               "han:-*-Kai-*-*-*-*-10-*-*-*-*-*-iso10646-1"
               "cyrillic:-*-Trebuchet$MS-*-*-*-*-10-*-*-*-*-*-iso10646-1")
             ",")
  "String of fontset spec of the standard fontset.
This defines a fontset consisting of the Courier and other fonts that
come with OS X.
See the documentation of `create-fontset-from-fontset-spec' for the format.")

;; Conditional on new-fontset so bootstrapping works on non-GUI compiles.
(when (fboundp 'new-fontset)
  ;; Setup the default fontset.
  (create-default-fontset)
  ;; Create the standard fontset.
  (condition-case err
      (create-fontset-from-fontset-spec ns-standard-fontset-spec t)
    (error (display-warning
            'initialization
            (format "Creation of the standard fontset failed: %s" err)
            :error))))

(defvar ns-reg-to-script)               ; nsfont.m

;; This maps font registries (not exposed by NS APIs for font selection) to
;; Unicode scripts (which can be mapped to Unicode character ranges which are).
;; See ../international/fontset.el
(setq ns-reg-to-script
      '(("iso8859-1" . latin)
	("iso8859-2" . latin)
	("iso8859-3" . latin)
	("iso8859-4" . latin)
	("iso8859-5" . cyrillic)
	("microsoft-cp1251" . cyrillic)
	("koi8-r" . cyrillic)
	("iso8859-6" . arabic)
	("iso8859-7" . greek)
	("iso8859-8" . hebrew)
	("iso8859-9" . latin)
	("iso8859-10" . latin)
	("iso8859-11" . thai)
	("tis620" . thai)
	("iso8859-13" . latin)
	("iso8859-14" . latin)
	("iso8859-15" . latin)
	("iso8859-16" . latin)
	("viscii1.1-1" . latin)
	("jisx0201" . kana)
	("jisx0208" . han)
	("jisx0212" . han)
	("jisx0213" . han)
	("gb2312.1980" . han)
	("gb18030" . han)
	("gbk-0" . han)
	("big5" . han)
	("cns11643" . han)
	("sisheng_cwnn" . bopomofo)
	("ksc5601.1987" . hangul)
	("ethiopic-unicode" . ethiopic)
	("is13194-devanagari" . indian-is13194)
	("iso10646.indian-1" . devanagari)))


;;;; Pasteboard support.

(declare-function ns-get-selection-internal "nsselect.m" (buffer))
(declare-function ns-store-selection-internal "nsselect.m" (buffer string))

(define-obsolete-function-alias 'ns-get-cut-buffer-internal
  'ns-get-selection-internal "24.1")
(define-obsolete-function-alias 'ns-store-cut-buffer-internal
  'ns-store-selection-internal "24.1")


(defun ns-get-pasteboard ()
  "Returns the value of the pasteboard."
  (ns-get-selection-internal 'CLIPBOARD))

(defun ns-set-pasteboard (string)
  "Store STRING into the pasteboard of the Nextstep display server."
  ;; Check the data type of STRING.
  (if (not (stringp string)) (error "Nonstring given to pasteboard"))
  (ns-store-selection-internal 'CLIPBOARD string))

;; We keep track of the last text selected here, so we can check the
;; current selection against it, and avoid passing back our own text
;; from x-selection-value.
(defvar ns-last-selected-text nil)

;; Return the value of the current Nextstep selection.  For
;; compatibility with older Nextstep applications, this checks cut
;; buffer 0 before retrieving the value of the primary selection.
(defun x-selection-value ()
  (let (text)
    ;; Consult the selection.  Treat empty strings as if they were unset.
    (or text (setq text (ns-get-pasteboard)))
    (if (string= text "") (setq text nil))
    (cond
     ((not text) nil)
     ((eq text ns-last-selected-text) nil)
     ((string= text ns-last-selected-text)
      ;; Record the newer string, so subsequent calls can use the `eq' test.
      (setq ns-last-selected-text text)
      nil)
     (t
      (setq ns-last-selected-text text)))))

(defun ns-copy-including-secondary ()
  (interactive)
  (call-interactively 'kill-ring-save)
  (ns-store-selection-internal 'SECONDARY
			       (buffer-substring (point) (mark t))))
(defun ns-paste-secondary ()
  (interactive)
  (insert (ns-get-selection-internal 'SECONDARY)))


;;;; Scrollbar handling.

(global-set-key [vertical-scroll-bar down-mouse-1] 'ns-handle-scroll-bar-event)
(global-unset-key [vertical-scroll-bar mouse-1])
(global-unset-key [vertical-scroll-bar drag-mouse-1])

(declare-function scroll-bar-scale "scroll-bar" (num-denom whole))

(defun ns-scroll-bar-move (event)
  "Scroll the frame according to a Nextstep scroller event."
  (interactive "e")
  (let* ((pos (event-end event))
         (window (nth 0 pos))
         (scale (nth 2 pos)))
    (with-current-buffer (window-buffer window)
      (cond
       ((eq (car scale) (cdr scale))
	(goto-char (point-max)))
       ((= (car scale) 0)
	(goto-char (point-min)))
       (t
	(goto-char (+ (point-min) 1
		      (scroll-bar-scale scale (- (point-max) (point-min)))))))
      (beginning-of-line)
      (set-window-start window (point))
      (vertical-motion (/ (window-height window) 2) window))))

(defun ns-handle-scroll-bar-event (event)
  "Handle scroll bar EVENT to emulate Nextstep style scrolling."
  (interactive "e")
  (let* ((position (event-start event))
	 (bar-part (nth 4 position))
	 (window (nth 0 position))
	 (old-window (selected-window)))
    (cond
     ((eq bar-part 'ratio)
      (ns-scroll-bar-move event))
     ((eq bar-part 'handle)
      (if (eq window (selected-window))
	  (track-mouse (ns-scroll-bar-move event))
        ;; track-mouse faster for selected window, slower for unselected.
	(ns-scroll-bar-move event)))
     (t
      (select-window window)
      (cond
       ((eq bar-part 'up)
	(goto-char (window-start window))
	(scroll-down 1))
       ((eq bar-part 'above-handle)
	(scroll-down))
       ((eq bar-part 'below-handle)
	(scroll-up))
       ((eq bar-part 'down)
	(goto-char (window-start window))
	(scroll-up 1)))
      (select-window old-window)))))


;;;; Color support.

;; Functions for color panel + drag
(defun ns-face-at-pos (pos)
  (let* ((frame (car pos))
         (frame-pos (cons (cadr pos) (cddr pos)))
         (window (window-at (car frame-pos) (cdr frame-pos) frame))
         (window-pos (coordinates-in-window-p frame-pos window))
         (buffer (window-buffer window))
         (edges (window-edges window)))
    (cond
     ((not window-pos)
      nil)
     ((eq window-pos 'mode-line)
      'mode-line)
     ((eq window-pos 'vertical-line)
      'default)
     ((consp window-pos)
      (with-current-buffer buffer
        (let ((p (car (compute-motion (window-start window)
                                      (cons (nth 0 edges) (nth 1 edges))
                                      (window-end window)
                                      frame-pos
                                      (- (window-width window) 1)
                                      nil
                                      window))))
          (cond
           ((eq p (window-point window))
            'cursor)
           ((and mark-active (< (region-beginning) p) (< p (region-end)))
            'region)
           (t
	    (let ((faces (get-char-property p 'face window)))
	      (if (consp faces) (car faces) faces)))))))
     (t
      nil))))

(defvar ns-input-color)			; nsterm.m

(defun ns-set-foreground-at-mouse ()
  "Set the foreground color at the mouse location to `ns-input-color'."
  (interactive)
  (let* ((pos (mouse-position))
         (frame (car pos))
         (face (ns-face-at-pos pos)))
    (cond
     ((eq face 'cursor)
      (modify-frame-parameters frame (list (cons 'cursor-color
                                                 ns-input-color))))
     ((not face)
      (modify-frame-parameters frame (list (cons 'foreground-color
                                                 ns-input-color))))
     (t
      (set-face-foreground face ns-input-color frame)))))

(defun ns-set-background-at-mouse ()
  "Set the background color at the mouse location to `ns-input-color'."
  (interactive)
  (let* ((pos (mouse-position))
         (frame (car pos))
         (face (ns-face-at-pos pos)))
    (cond
     ((eq face 'cursor)
      (modify-frame-parameters frame (list (cons 'cursor-color
                                                 ns-input-color))))
     ((not face)
      (modify-frame-parameters frame (list (cons 'background-color
                                                 ns-input-color))))
     (t
      (set-face-background face ns-input-color frame)))))

;; Set some options to be as Nextstep-like as possible.
(setq frame-title-format t
      icon-title-format t)


(defvar ns-initialized nil
  "Non-nil if Nextstep windowing has been initialized.")

(declare-function ns-list-services "nsfns.m" ())
(declare-function x-open-connection "nsfns.m"
                  (display &optional xrm-string must-succeed))
(declare-function ns-set-resource "nsfns.m" (owner name value))

;; Do the actual Nextstep Windows setup here; the above code just
;; defines functions and variables that we use now.
(defun ns-initialize-window-system ()
  "Initialize Emacs for Nextstep (Cocoa / GNUstep) windowing."
  (cl-assert (not ns-initialized))

  ;; PENDING: not needed?
  (setq command-line-args (x-handle-args command-line-args))

  (x-open-connection (system-name) nil t)

  (dolist (service (ns-list-services))
      (if (eq (car service) 'undefined)
	  (ns-define-service (cdr service))
	(define-key global-map (vector (car service))
	  (ns-define-service (cdr service)))))

  (if (and (eq (get-lisp-resource nil "NXAutoLaunch") t)
	   (eq (get-lisp-resource nil "HideOnAutoLaunch") t))
      (add-hook 'after-init-hook 'ns-do-hide-emacs))

  ;; FIXME: This will surely lead to "MODIFIED OUTSIDE CUSTOM" warnings.
  (menu-bar-mode (if (get-lisp-resource nil "Menus") 1 -1))

  ;; OS X Lion introduces PressAndHold, which is unsupported by this port.
  ;; See this thread for more details:
  ;; http://lists.gnu.org/archive/html/emacs-devel/2011-06/msg00505.html
  (ns-set-resource nil "ApplePressAndHoldEnabled" "NO")

  (x-apply-session-resources)
  (setq ns-initialized t))

(add-to-list 'display-format-alist '("\\`ns\\'" . ns))
(add-to-list 'handle-args-function-alist '(ns . x-handle-args))
(add-to-list 'frame-creation-function-alist '(ns . x-create-frame-with-faces))
(add-to-list 'window-system-initialization-alist '(ns . ns-initialize-window-system))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Implementation of Input Method Extension for MacOS X
;; written by Taiichi Hashimoto <taiichi2@mac.com>
;;

(defvar mac-input-method-parameters
  '(
    ("com.apple.inputmethod.Kotoeri.Roman"
     (title . "A")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.Kotoeri.Japanese"
     (title . "あ")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.Kotoeri.Japanese.Katakana"
     (title . "ア")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.Kotoeri.Japanese.FullWidthRoman"
     (title . "Ａ")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.Kotoeri.Japanese.HalfWidthKana"
     (title . "ｱ")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.kotoeri.Ainu"
     (title . "アイヌ")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.Korean.2SetKorean"
     (title . "가2")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.Korean.3SetKorean"
     (title . "가3")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.Korean.390Sebulshik"
     (title . "가5")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.Korean.GongjinCheongRomaja"
     (title . "가G")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.Korean.HNCRomaja"
     (title . "가H")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.Tamil.AnjalIM"
     (title . "Anjal")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.Tamil.Tamil99"
     (title . "Tamil")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.VietnameseIM.VietnameseSimpleTelex"
     (title . "ST")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.VietnameseIM.VietnameseTelex"
     (title . "TX")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.VietnameseIM.VietnameseVNI"
     (title . "VN")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.VietnameseIM.VietnameseVIQR"
     (title . "VQ")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.SCIM.ITABC"
     (title . "拼")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.SCIM.WBX"
     (title . "型")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.SCIM.WBH"
     (title . "画")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.TCIM.Zhuyin"
     (title . "注")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.TCIM.Pinyin"
     (title . "拼")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.TCIM.Cangjie"
     (title . "倉")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.TCIM.Jianyi"
     (title . "速")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.TCIM.Dayi"
     (title . "易")
     (cursor-color)
     (cursor-type))
    ("com.apple.inputmethod.TCIM.Hanin"
     (title . "漢")
     (cursor-color)
     (cursor-type))
    ("com.google.inputmethod.Japanese.Roman"
     (title . "G")
     (cursor-color)
     (cursor-type))
    ("com.google.inputmethod.Japanese.base"
     (title . "ぐ")
     (cursor-color)
     (cursor-type))
    ("com.google.inputmethod.Japanese.Katakana"
     (title . "グ")
     (cursor-color)
     (cursor-type))
    ("com.google.inputmethod.Japanese.FullWidthRoman"
     (title . "Ｇ")
     (cursor-color)
     (cursor-type))
    ("com.google.inputmethod.Japanese.HalfWidthKana"
     (title . "ｸﾞ")
     (cursor-color)
     (cursor-type))
    ("jp.monokakido.inputmethod.Kawasemi.Roman"
     (title . "K")
     (cursor-color)
     (cursor-type))
    ("jp.monokakido.inputmethod.Kawasemi.Japanese"
     (title . "か")
     (cursor-color)
     (cursor-type))
    ("jp.monokakido.inputmethod.Kawasemi.Japanese.Katakana"
     (title . "カ")
     (cursor-color)
     (cursor-type))
    ("jp.monokakido.inputmethod.Kawasemi.Japanese.FullWidthRoman"
     (title . "Ｋ")
     (cursor-color)
     (cursor-type))
    ("jp.monokakido.inputmethod.Kawasemi.Japanese.HalfWidthKana"
     (title . "ｶ")
     (cursor-color)
     (cursor-type))
    ("jp.monokakido.inputmethod.Kawasemi.Japanese.HalfWidthRoman"
     (title . "_K")
     (cursor-color)
     (cursor-type))
    ("jp.monokakido.inputmethod.Kawasemi.Japanese.Code"
     (title . "C")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok21.Roman"
     (title . "A")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok21.Japanese"
     (title . "あ")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok21.Japanese.Katakana"
     (title . "ア")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok21.Japanese.FullWidthRoman"
     (title . "英")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok21.Japanese.HalfWidthEiji"
     (title . "半英")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok22.Roman"
     (title . "A")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok22.Japanese"
     (title . "あ")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok22.Japanese.Katakana"
     (title . "ア")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok22.Japanese.FullWidthRoman"
     (title . "英")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok22.Japanese.HalfWidthEiji"
     (title . "半英")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok23.Roman"
     (title . "A")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok23.Japanese"
     (title . "あ")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok23.Japanese.Katakana"
     (title . "ア")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok23.Japanese.FullWidthRoman"
     (title . "英")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok23.Japanese.HalfWidthEiji"
     (title . "半英")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok24.Roman"
     (title . "A")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok24.Japanese"
     (title . "あ")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok24.Japanese.Katakana"
     (title . "ア")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok24.Japanese.FullWidthRoman"
     (title . "英")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok24.Japanese.HalfWidthEiji"
     (title . "半英")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok25.Roman"
     (title . "A")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok25.Japanese"
     (title . "あ")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok25.Japanese.Katakana"
     (title . "ア")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok25.Japanese.FullWidthRoman"
     (title . "英")
     (cursor-color)
     (cursor-type))
    ("com.justsystems.inputmethod.atok25.Japanese.HalfWidthEiji"
     (title . "半英")
     (cursor-color)
     (cursor-type))
    )
  "Alist of Mac script code vs parameters for input method on MacOSX.")


(defun mac-get-input-method-parameter (is key)
  "Function to get a parameter of a input method."
  (interactive)
  (assq key (cdr (assoc is mac-input-method-parameters))))

(defun mac-get-input-method-title (&optional input-source)
  "Return input method title of input source.
   If input-source is nil, return one of current frame."
  (if input-source
      (cdr (mac-get-input-method-parameter input-source 'title))
    current-input-method-title))

(defun mac-get-cursor-type (&optional input-source)
  "Return cursor type of input source.
   If input-source is nil, return one of current frame."
  (if input-source
      (or (cdr (mac-get-input-method-parameter input-source 'cursor-type))
	  (cdr (assq 'cursor-type default-frame-alist))
	  cursor-type)
    (cdr (assq 'cursor-type (frame-parameters (selected-frame))))))

(defun mac-get-cursor-color (&optional input-source)
  "Return cursor color of input source.
   If input-source is nil, return one of current frame."
  (if input-source
      (or (cdr (mac-get-input-method-parameter input-source 'cursor-color))
	  (cdr (assq 'cursor-color default-frame-alist)))
    (cdr (assq 'cursor-color (frame-parameters (selected-frame))))))


(defun mac-set-input-method-parameter (is key value)
  "Function to set a parameter of a input method."
  (let* ((is-param (assoc is mac-input-method-parameters))
         (param (assq key is-param)))
    (if is-param
	(if param
	    (setcdr param value)
	  (setcdr is-param (cons (cons key value) (cdr is-param))))
      (setq mac-input-method-parameters
	    (cons (list is (cons key value))
		  mac-input-method-parameters)))))


(defun mac-input-method-update (is)
  "Funtion to update parameters of a input method."
  (interactive)

  (let ((title (mac-get-input-method-title is))
        (type (mac-get-cursor-type is))
        (color (mac-get-cursor-color is)))
    (if (and title (not (equal title (mac-get-input-method-title))))
	(setq current-input-method-title title))
    (if (and type (not (equal type (mac-get-cursor-type))))
	(setq cursor-type type))
    (if (and color (not (equal color (mac-get-cursor-color))))
	(set-cursor-color color))
    (force-mode-line-update)
    (if isearch-mode (isearch-update))))


(defun mac-toggle-input-method (&optional arg)
  "Function to toggle input method on MacOSX."
  (interactive)
  
  (if arg
      (progn
	(make-local-variable 'input-method-function)
	(setq inactivate-current-input-method-function 'mac-toggle-input-method)
	(setq input-method-function nil)
	(setq describe-current-input-method-function nil)
	(mac-toggle-input-source t))
    (kill-local-variable 'input-method-function)
    (setq describe-current-input-method-function nil)
    (mac-toggle-input-source nil)))


(defun mac-change-language-to-us ()
  "Function to change language to us."
  (interactive)
  (mac-toggle-input-method nil))


(defun mac-handle-input-method-change ()
  "Function run when a input method change."
  (interactive)

  (if (equal default-input-method "MacOSX")
      (let ((input-source (mac-get-current-input-source))
	  (ascii-capable (mac-input-source-is-ascii-capable)))
	
	(cond ((and (not current-input-method) (not ascii-capable))
	       (set-input-method "MacOSX"))
	      ((and (equal current-input-method "MacOSX") ascii-capable)
	       (toggle-input-method nil)))
	(mac-input-method-update input-source))))

;;
;; Emacs input method for input method on MacOSX.
;;
(register-input-method "MacOSX" "MacOSX" 'mac-toggle-input-method
		       "Mac" "Input Method on MacOSX System")


;;
;; Minor mode of using input methods on MacOS X
;;
(define-minor-mode mac-input-method-mode
  "Use input methods on MacOSX."
  :init-value nil
  :group 'ns
  :global t

  (if mac-input-method-mode
      (progn
	(setq default-input-method "MacOSX")
	(add-hook 'minibuffer-setup-hook 'mac-change-language-to-us)
	(mac-translate-from-yen-to-backslash))
    (setq default-input-method nil)))

;;
;; Valiable and functions to pass key(shortcut) to system.
;;
(defvar mac-keys-passed-to-system nil
  "A list of keys passed to system on MacOSX.")

(defun mac-add-key-passed-to-system (key)
  (let ((shift   '(shift shft))
	(control '(control ctrl ctl))
	(option  '(option opt alternate alt))
	(command '(command cmd)))

    (add-to-list 'mac-keys-passed-to-system
		 (cond ((symbolp key)
			(cond ((memq key shift)
			       (cons ns-shift-key-mask nil))
			      ((memq key control)
			       (cons ns-control-key-mask nil))
			      ((memq key option)
			       (cons ns-alternate-key-mask nil))
			      ((memq key command)
			       (cons ns-command-key-mask nil))
			      (t (cons nil nil))))
		       ((numberp key) (cons 0 key))
		       ((listp key)
			(let ((l key) (k nil) (m 0))
			  (while l
			    (cond ((memq (car l) shift)
				   (setq m (logior m ns-shift-key-mask)))
				  ((memq (car l) control)
				   (setq m (logior m ns-control-key-mask)))
				  ((memq (car l) option)
				   (setq m (logior m ns-alternate-key-mask)))
				  ((memq (car l) command)
				       (setq m (logior m ns-command-key-mask)))
				  ((numberp (car l))
				   (if (not k) (setq k (car l)))))
			    (setq l (cdr l)))
			  (cons m k)))
		       (t (cons nil nil))))))


;;
;; Entry Emacs event for inline input method on MacOSX.
;;
(define-key special-event-map
  [mac-change-input-method] 'mac-handle-input-method-change)
      
;;
;; Convert yen to backslash for JIS keyboard.
;;
(defun mac-translate-from-yen-to-backslash () 
  ;; Convert yen to backslash for JIS keyboard.
  (interactive)

  (define-key global-map [165] nil)
  (define-key global-map [2213] nil)
  (define-key global-map [3420] nil)
  (define-key global-map [67109029] nil)
  (define-key global-map [67111077] nil)
  (define-key global-map [8388773] nil)
  (define-key global-map [134219941] nil)
  (define-key global-map [75497596] nil)
  (define-key global-map [201328805] nil)
  (define-key function-key-map [165] [?\\])
  (define-key function-key-map [2213] [?\\]) ;; for Intel
  (define-key function-key-map [3420] [?\\]) ;; for PowerPC
  (define-key function-key-map [67109029] [?\C-\\])
  (define-key function-key-map [67111077] [?\C-\\])
  (define-key function-key-map [8388773] [?\M-\\])
  (define-key function-key-map [134219941] [?\M-\\])
  (define-key function-key-map [75497596] [?\C-\M-\\])
  (define-key function-key-map [201328805] [?\C-\M-\\])
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(provide 'ns-win)

;;; ns-win.el ends here
