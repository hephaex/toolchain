;; hephaex emacs setting 4025
;; init.el

;(setq custom-dir (expand-file-name "custom" user-emacs-directory))
;(setq plugins-dir (expand-file-name "plugins" user-emacs-directory))
;(setq load-path
;      (cons (expand-file-name "/Users/mscho/Simon/llvm/utils/emacs") load-path))
;(require 'llvm-mode)
;(setq load-path
;      (cons (expand-file-name "/Users/mscho/Simon/llvm/utils/emacs") load-path))
;(require 'tablegen-mode)

;(add-to-list 'load-path custom-dir)
;(add-to-list 'load-path plugins-dir)

(setenv "PATH"
	(concat
	 "/usr/local/bin" ":"
	 "/usr/local/sbin" ":"
	 (getenv "PATH")
	 ))

;(add-to-list 'load-path "/usr/local/share/emacs/site-lisp/magit")
;(require 'magit)

(defun set-exec-path-from-shell-PATH ()
  (let ((path-from-shell (replace-regexp-in-string
                          "[ \t\n]*$"
                          ""
                          (shell-command-to-string "$SHELL --login -i -c 'echo $PATH'"))))
    (setenv "PATH" path-from-shell)
    (setq eshell-path-env path-from-shell) ; for eshell users
    (setq exec-path (split-string path-from-shell path-separator))))

; nxml
(setq auto-mode-alist (cons '("\\.xml$" . nxml-mode) auto-mode-alist))
(setq auto-mode-alist (cons '("\\.xsl$" . nxml-mode) auto-mode-alist))
(setq auto-mode-alist (cons '("\\.xhtml$" . nxml-mode) auto-mode-alist))
(setq auto-mode-alist (cons '("\\.page$" . nxml-mode) auto-mode-alist))
(autoload 'xml-mode "nxml" "XML editing mode" t)
(eval-after-load 'rng-loc
  '(add-to-list 'rng-schema-location-files "~/.schema/schemas.xml"))
(global-set-key [C-return] 'completion-at-point)

;(when window-system (set-exec-path-from-shell-PATH))
;(add-to-list 'load-path "~/.emacs.d/plugins/ecb-2.40/")
;(require 'cl)
;(require 'ecb)
;(require 'xcscope)
;(cscope-setup)

;(defmacro when-linux (&rest body)
;  (list 'if (string-match "linux" (prin1-to-string system-type))
;	(cons 'progn body)))

;(defmacro when-windows (&rest body)
;  (list 'if (string-match "windows" (prin1-to-string system-type))
;	(cons 'progn body)))

;(defmacro when-version-24 (&rest body)
;  (list 'if (>= emacs-major-version 24)
;	(cons 'progn body)))

;;(require 'ms-hangul)
;(when enable-multibyte-characters
;  (set-language-environment "Korean")
;  (setq-default file-name-coding-system 'utf-8)
;  (setq default-input-method "korean-hangul")
;  (prefer-coding-system 'utf-8)
;  (set-default-coding-systems 'utf-8)
;  )

;(when (eq system-type 'windows-nt)
;  (setq-default file-name-coding-system 'euc-kr)
;  )

;(when (eq system-type 'windows-nt)
;  (global-set-key (kbd "S-SPC") 'toggle-input-method)
;  (global-set-key (kbd "<Hangul>") 'toggle-input-method)
;  (global-set-key (kbd "<Hangul_Hanja>") 'hangul-to-hanja-conversion)
;  )

;; (custom-set-variables
;;  '(default-input-method "korean-hangul"))

;; (set-selection-coding-system
;;  (cond ((eq system-type 'windows-nt) 'utf-8-dos)
;;        (t 'utf-8)))

;;(provide 'ms-hangul)

;;(require 'ms-ui)
;; 줄 번호 출력
(add-hook 'find-file-hook (lambda () (linum-mode t)))

;;emacs를 실행하면 나타나는 시작 화면을 없애고 편집 파일만 보이게 합니다. 
(setq inhibit-startup-screen t)


(defun c-up-conditional-with-else-back()
  (interactive)
  (c-up-conditional-with-else -1))

;(defun ms-ui-load-theme (theme)
;  (interactive)
;  (add-to-list 'custom-theme-load-path plugins-dir)
;  (if (>= emacs-major-version 24)
      ;; see http://batsov.com/articles/2012/02/19/color-theming-in-emacs-reloaded/
;      (load-theme theme t)
;    (error "Can't load the theme, mismatch with the emacs version.")))

;(defun ms-ui-fullscreen ()
;  (interactive)
;  (set-frame-parameter nil 'fullscreen
;		       (if (frame-parameter nil 'fullscreen) nil 'fullboth)))

(setq inhibit-splash-screen t)

;; load the color theme. ex) zenburn, tango-2, solarized-light,
;; solarized-dark, tron...
;(ms-ui-load-theme 'solarized-light)
;(ms-ui-load-theme 'zenburn)

;; ;; Turn off early to avoid momentary display.
;; (mapc
;;  (lambda (mode)
;;    (if (fboundp mode)
;;        (funcall mode -1)))
;;  '(menu-bar-mode tool-bar-mode scroll-bar-mode))

;; remove the tool-bar
;;(when window-system (tool-bar-mode -1))

(set-default 'cursor-type 'box)


;; face setting
;(when window-system
  ;; (set-face-font 'default "-outline-Bitstream Vera Sans Mono-normal-normal-normal-mono-15-*-*-*-c-*-iso10646-1")
  ;; (set-face-font 'default "-outline-DejaVu Sans Mono-normal-normal-normal-mono-15-*-*-*-c-*-iso10646-1")
;  (set-face-font 'default "-outline-Monaco-normal-normal-normal-*-*-*-*-*-p-*-iso10646-1")
  ;; (set-face-font 'default "-outline-Consolas-normal-normal-normal-mono-*-*-*-*-c-*-fontset-consolas14")
  ;; (set-face-font 'default "-outline-Consolas-normal-normal-normal-mono-15-*-*-*-c-*-iso10646-1")
  ;; (set-face-font 'default "-microsoft-Consolas-normal-normal-normal-*-16-*-*-*-m-0-iso10646-1")
  ;;(set-fontset-font "fontset-default" 'hangul '("malgun gothic" . "unicode-bmp"))

;  (when (equal current-language-environment "Korean")
;    (set-fontset-font "fontset-default" '(#x1100 . #xffdc)  '("NanumGothicCoding" . "unicode-bmp"))) ;; unicode region of Hangul
  ;;(set-fontset-font "fontset-default" '(#x1100 . #xffdc)  '("malgun gothic" . "unicode-bmp")) ;; unicode region of Hangul
;  (set-fontset-font "fontset-default" 'kana '("ms mincho" . "unicode-bmp"))
;  (set-fontset-font "fontset-default" 'han '("ms mincho" . "unicode-bmp"))
  ;; (set-fontset-font "fontset-default" 'cjk-misc '("ms mincho" . "unicode-bmp"))
;  )

;(provide 'ms-ui)

;;(require 'ms-common)
;(defun ms-use-package (name)
;  "If feature NAME is not installed, install it from the ELPA;
;then load it."
;  (interactive)
;  (when-version-24
;   (if (not (require name nil t))
;       (package-install name)
;     t)
;   (require name nil t)))

;; convert a buffer from DOS `^M' end of lines to Unix end of lines
(defun dos-to-unix ()
  "Cut all visible ^M from the current buffer."
  (interactive)
  (save-excursion
    (goto-char (point-min))
    (while (search-forward "\r" nil t)
      (replace-match ""))))

;; convert a buffer from Unix end of lines to DOS `^M' end of lines
(defun unix-to-dos ()
  (interactive)
  (save-excursion
    (goto-char (point-min))
    (while (search-forward "\n" nil t)
      (replace-match "\r\n"))))

;; for iswitchb
(defun iswitchb-local-keys ()
  (mapc (lambda (K)
	  (let* ((key (car K)) (fun (cdr K)))
	    (define-key iswitchb-mode-map (edmacro-parse-keys key) fun)))
	'(("<right>" . iswitchb-next-match)
	  ("<left>"  . iswitchb-prev-match)
	  ("<up>"    . ignore             )
	  ("<down>"  . ignore             ))))

;; Terminal at Your Fingertips. http://emacsredux.com/blog/2013/03/29/terminal-at-your-fingertips
(defun visit-term-buffer ()
  "Create or visit a terminal buffer."
  (interactive)
  (if (not (get-buffer "*ansi-term*"))
      (progn
       (split-window-sensibly (selected-window))
       (other-window 1)
       (ansi-term (getenv "SHELL")))
    (switch-to-buffer-other-window "*ansi-term*")))


;; env setting
;(when-windows
; (setenv "PATH"
;	 (concat
;	  "C:/cygwin/usr/local/bin" ";"
;	  "C:/cygwin/usr/bin" ";"
;	  "C:/cygwin/bin" ";"
;	  (getenv "PATH")))
;
; (add-to-list 'exec-path
;              "C:/pkg/global/bin/"
;              "C:/cygwin/bin/")
; )

;; Use left/right arrow keys for the iswitchb
(add-hook 'iswitchb-define-mode-map-hook 'iswitchb-local-keys)

;; ANSI colors for the Emacs Shell(eshell)
(add-hook 'shell-mode-hook 'ansi-color-for-comint-mode-on)

;; (provide 'ms-common)


;;(require 'ms-programming)
;; yasnippet
;(add-to-list 'load-path "~/.emacs.d/plugins/yasnippet")
;(require 'yasnippet)
;(yas-global-mode 1)

;(custon-set-variables
; '(file-column 80)
; '(c++_indent-level 2)
; '(c-basic-offset 2)
; '(indent-tabs-mode nil))

;(c-add-style "llvm.org"
;	     '((fill-column . 80)
;	       (c++-indent-level . 2)
;	       (c-basic-offset 2)
;	       (indent-table-mode . nil)
;	       (c-offset-alist . ((innamespace 0)))))
(add-hook 'c-mode-hook
	  (function
	   (lambda nil
	     (if (string-match "llvm" buffer-file-name)
		 (progn
		   (c-set-style "llvm.org")
		   )
	       ))))
(add-hook 'c++-mode-hook
	  (function
	   (lambda nil
	     (if (string-match "llvm" buffer-file-name)
		 (progn
		   (c-set-style "llvm.org")
		   )
	       ))))
 
;;(add-hook 'c-mode-common-hook
;;	  (lambda ()
;;	    (c-set-style "linux")))

;; linux-c-mode
;; (add-hook 'c-mode-common-hook
;;           (lambda ()
;;             ;; (gtags-mode 1)
;;             (gtags-create-or-update)))

;;(defun ms-linux-c-mode()
;;  "C mode with adjusted defaults for use with the Linux kernel."
;;  (interactive)
;;  (c-mode)
;;  (setq c-indent-level 8)
;;  (setq c-brace-imaginary-offset 0)
;;  (setq c-brace-offset -8)
;;  (setq c-argdecl-indent 8)
;;  (setq c-label-offset -8)
;;  (setq c-continued-statement-offset 8)
;;  (setq indent-tabs-mode nil)
;;  (setq tab-width 8))

;; ANSI colors for the compilation mode
;(add-hook 'compilation-mode-hook 'ansi-color-for-comint-mode-on)

;; java mode hook
;(add-hook 'java-mode-hook
;          (lambda ()
;            (setq c-basic-offset 4
;                  tab-width 4
;                  indent-tabs-mode nil)))

;; make-regexp
;(autoload 'make-regexp "make-regexp"
;  "Return a regexp to match a string item in STRINGS.")
;(autoload 'make-regexps "make-regexp"
;  "Return a regexp to REGEXPS.")

;; python
;(require 'python)
;(require 'auto-complete)
;(require 'python-mode)
;(autoload 'python-mode "python-mode" "Python editing mode" t)
;(add-to-list 'auto-mode-alist '("\\.py$" .python-mode))
;(add-to-list 'interpreter-mode-alist '("python" .python-mode))

;; ipython setting
;;(require 'ipython)
;;(setq py

;; For auto-complete
;(require 'auto-complete-config)
;(add-to-list 'ac-dictionary-directories "~/.emacs.d/plugins/ac-dict")
;(ac-config-default)

;; -----------------------------------------------------------------------------
;; hideshow for programming
;; -----------------------------------------------------------------------------
(load-library "hideshow")
;; hide상태에서 goto-line했을 때 자동으로 show로 변경
(defadvice goto-line (after expand-after-goto-line
                            activate compile)
  "hideshow-expand affected block when using goto-line in a collapsed buffer"
  (save-excursion
    (hs-show-block)))

;(add-hook 'c++-mode-hook 'hs-minor-mode)

;(global-set-key [f5] 'hs-toggle-hiding)
;(global-set-key [f6] 'hs-show-all)
;(global-set-key [f7] 'hs-hide-all)


;; -----------------------------------------------------------------------------
;; slime
;; -----------------------------------------------------------------------------
;(add-to-list 'load-path (concat plugins-dir "/slime"))

;(when-linux
; (setq inferior-lisp-program "~/cl/bin/sbcl"))

;(when-windows
; (setq inferior-lisp-program "C:/devel/sbcl/1.1.8/sbcl.exe"))

;; lisp-indent-function 'common-lisp-indent-function
;; slime-complete-symbol-function 'slime-fuzzy-complete-symbol
;; slime-startup-animation nil)

;(require 'slime)
;(slime-setup '(slime-fancy slime-fuzzy slime-c-p-c))
;(setq slime-net-coding-system 'utf-8-unix)

;(setq common-lisp-hyperspec-root "http://www.lispworks.com/documentation/HyperSpec/")
;(define-key slime-mode-map (kbd "TAB") 'slime-indent-and-complete-symbol)

;(defun slime-common-lisp ()
;  (interactive)
  ;; (when-linux
  ;;  (setq inferior-lisp-program "/usr/bin/sbcl"))
  ;; (when-windows
  ;;  (setq inferior-lisp-program "C:/pkg/clisp-2.49/clisp.exe"))
  ;; (add-to-list 'load-path "~/.emacs.d/plugins/slime/")
;  (require 'slime)
;  (slime-setup '(slime-repl))
;  (slime))

;(defun slime-clojure ()
;  (interactive)
;  ;; (add-to-list 'load-path "~/.emacs.d/plugins/slime/")
;  (require 'slime)
;  (slime-setup '(slime-repl))
;  (slime-connect "localhost" 4005))

;(add-hook 'lisp-mode-hook (lambda () (slime-mode t)))
;(add-hook 'inferior-lisp-mode-hook (lambda () (inferior-slime-mode t)))

;; emacs lisp mode
;(defun goldmund-emacs-lisp-mode-init ()
;  (interactive)
;  (imenu-add-to-menubar "Functions")
;  (define-key emacs-lisp-mode-map [f6] 'eval-buffer)
;  (define-key emacs-lisp-mode-map [(meta f6)] 'emacs-lisp-byte-compile-and-load)
;  (define-key emacs-lisp-mode-map [return] 'newline-and-indent)
;  ;; (define-key emacs-lisp-mode-map [?\C-c?t] 'xsteve-trace-function)
;  (modify-syntax-entry ?- "w")
;  (hs-minor-mode t)
;  (turn-on-eldoc-mode))

;(add-hook 'emacs-lisp-mode-hook 'goldmund-emacs-lisp-mode-init)
;(add-hook 'emacs-lisp-mode-hook 'hs-minor-mode)
;(add-hook 'emacs-lisp-mode-hook (lambda () (setq indent-tabs-mode nil)))

;; ediff
;(setq ediff-split-window-function 'split-window-vertically)

;;(provide 'ms-programming)

;;(require 'ms-orgmode)
;; Set to the location of your Org files on your local system
;(setq org-directory "~/org")
;(setq org-agenda-files '("~/org"))
;(setq org-todo-keywords
;'((sequence "TODO(t)" "WAIT(w)" "|" "DOWN(d)" "SOMEDAY(s)")))
;(setq org-log-done 'time)
;
;; Set to the name of the file where new notes will be stored
;(setq org-mobile-inbox-for-pull (expand-file-name "inbox.org" org-directory))
;(setq org-mobile-files '("~/org/todo.org"))

;; Set to <your Dropbox root directory>/MobileOrg.
;(setq org-mobile-agenda-files '("~/org/todo.org"
;				"~/org/study.org"
;				"~/org/travels.org"))
;(setq org-mobile-directory "~/Dropbox/Apps/MobileOrg")

(global-set-key "\C-cl" 'org-store-link)
(global-set-key "\C-cc" 'org-capture)
(global-set-key "\C-ca" 'org-agenda)
(global-set-key "\C-cb" 'org-iswitchb)

;; Calendar setting
;(require 'calendar)		      ; it's built-in.
;(calendar-set-date-style 'iso)	      ; set the "year/month/day" style

;;(provide 'ms-orgmode)

;;(require 'ms-writing)
;; -----------------------------------------------------------------------------
;; markdown markdown-mode
;; -----------------------------------------------------------------------------
;; markdown-mode
(autoload 'markdown-mode "markdown-mode.el" "Major mode for editing Markdown files" t)
(add-to-list 'auto-mode-alist '("\\.markdown\\'" . markdown-mode))
(add-to-list 'auto-mode-alist '("\\.mkd\\'" . markdown-mode))
(add-to-list 'auto-mode-alist '("\\.md\\'" . markdown-mode))
(add-to-list 'auto-mode-alist '("\\.text\\'" . markdown-mode))

;(custom-set-variables '(markdown-command "markdown_py"))

;; -----------------------------------------------------------------------------
;; graphviz dot-mode
;; -----------------------------------------------------------------------------
;; (require 'dot-mode)
;; (add-hook 'find-file-hooks 'dot-mode-on)
(autoload 'graphviz-dot-mode "graphviz-dot-mode" "graphviz-dot Editing Mode" t)
(add-to-list 'auto-mode-alist '("\\.dot\\'" . graphviz-dot-mode))

;; -----------------------------------------------------------------------------
;; orgernizer org-mode
;; -----------------------------------------------------------------------------
;; Set to the location of your Org files on your local system
;(setq org-directory "~/Document/org")
;(setq org-agenda-files '("~/Document/org"))
;(setq org-todo-keywords
;'((sequence "TODO(t)" "WAIT(w)" "|" "DOWN(d)" "SOMEDAY(s)")))
;(setq org-log-done 'time)
;
;; Set to the name of the file where new notes will be stored
;(setq org-mobile-inbox-for-pull (expand-file-name "inbox.org" org-directory))
;(setq org-mobile-files '("~/Document/org/todo.org"))

;; Set to <your Dropbox root directory>/MobileOrg.
;(setq org-mobile-agenda-files '("~/Document/org/todo.org"
;				"~/Document/org/study.org"
;				"~/Document/org/travels.org"))
;;(setq org-mobile-directory "~/Dropbox/Apps/MobileOrg")

(global-set-key "\C-cl" 'org-store-link)
(global-set-key "\C-cc" 'org-capture)
(global-set-key "\C-ca" 'org-agenda)
(global-set-key "\C-cb" 'org-iswitchb)

;; Calendar setting
;(require 'calendar)		      ; it's built-in.
;(calendar-set-date-style 'iso)	      ; set the "year/month/day" style

;; deft - http://jblevins.org/projects/deft/
;(when (ms-use-package 'deft)
;  (setq
;   deft-extension "org"
;   deft-directory (concat org-directory "/deft/")
;   deft-text-mode 'org-mode)
;  (global-set-key (kbd "<f9>") 'deft))

;(provide 'ms-writing)

;; key setting
;(when (eq system-type 'darwin) ;; mac specific settings
;  (setq mac-options-modifier 'meta)
;;  (setq mac-option-modifier 'alt)
;;  (setq mac-command-modifier 'meta)
;  (global-set-key [kp-delete] 'delete-char) ;; sets fn-delete to be right-delete
;  )
(global-set-key "\M-g" 'goto-line)
(global-set-key "\M-\," 'pop-tag-mark)  ; 
(global-set-key [\M-right] 'call-last-kbd-macro)
(fset 'find-next-tag "\C-u\256")        ; macro for C-u M-.
(fset 'find-prev-tag "\C-u-\256")       ; macro for C-u - M-.
(global-set-key "\M-]" 'find-next-tag)
(global-set-key "\M-[" 'find-prev-tag)
(global-set-key [C-return] 'semantic-complete-analyze-inline)
(global-set-key (kbd "C-c t") 'visit-term-buffer)
(global-set-key [f11] 'ms-ui-fullscreen)

;(if (not (getenv "TERM_PROGRAM"))
;     (setenv "PATH"
;            (shell-command-to-string "source $HOME/.bash_profile &amp;&amp; printf $PATH")))

;;(custom-set-faces
;; custom-set-faces was added by Custom.
 ;; If you edit it by hand, you could mess it up, so be careful.
 ;; Your init file should contain only one such instance.
 ;; If there is more than one, they won't work right.

;;(set-fontset-font "fontset-default" '(#x1100 . #xffdc) '("NanumGothic_Coding" . "unicode-bmp"))
;;(set-fontset-font "fontset-default" '(#xe0bc . #xf66e) '("NanumGothic_Coding" . "unicode-bmp"))

;)
 

(custom-set-variables
 ;; custom-set-variables was added by Custom.
 ;; If you edit it by hand, you could mess it up, so be careful.
 ;; Your init file should contain only one such instance.
 ;; If there is more than one, they won't work right.
 '(column-number-mode t)
 '(current-language-environment "UTF-8")
 '(global-auto-revert-mode 1)
 '(global-hl-line-mode 1)
 '(iswitchb-mode t)
 '(make-backup-files nil)
 '(markdown-command "markdown_py")
 '(show-paren-mode t)
 '(tool-bar-mode nil)
 '(truncate-lines t))

