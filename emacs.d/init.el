;; hephaex emacs setting 8705
;; init.el

;(setq custom-dir (expand-file-name "custom" user-emacs-directory))
;(setq plugins-dir (expand-file-name "plugins" user-emacs-directory))
;(add-to-list 'load-path custom-dir)
;(add-to-list 'load-path plugins-dir)

(setenv "PATH"
	(concat
	 "/usr/local/bin" ":"
	 "/usr/local/sbin" ":"
	 (getenv "PATH")
	 ))

;;(require 'ms-ui)
;; 줄 번호 출력
(add-hook 'find-file-hook (lambda () (linum-mode t)))

;;emacs를 실행하면 나타나는 시작 화면을 없애고 편집 파일만 보이게 합니다. 
(setq inhibit-startup-screen t)

(defun c-up-conditional-with-else-back()
  (interactive)
  (c-up-conditional-with-else -1))


(setq inhibit-splash-screen t)

;; load the color theme. ex) zenburn, tango-2, solarized-light,
;; solarized-dark, tron...
;(ms-ui-load-theme 'solarized-light)
;(ms-ui-load-theme 'zenburn)

;; remove the tool-bar
;;(when window-system (tool-bar-mode -1))

(set-default 'cursor-type 'box)

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

;; ANSI colors for the Emacs Shell(eshell)
(add-hook 'shell-mode-hook 'ansi-color-for-comint-mode-on)
 
;; make-regexp
;(autoload 'make-regexp "make-regexp"
;  "Return a regexp to match a string item in STRINGS.")
;(autoload 'make-regexps "make-regexp"
;  "Return a regexp to REGEXPS.")

;; python
(require 'python)
(require 'auto-complete)
(require 'python-mode)
(autoload 'python-mode "python-mode" "Python editing mode" t)
(add-to-list 'auto-mode-alist '("\\.py$" .python-mode))
(add-to-list 'interpreter-mode-alist '("python" .python-mode))

;; ipython setting
(require 'ipython)

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

;; ediff
(setq ediff-split-window-function 'split-window-vertically)


(global-set-key "\C-cl" 'org-store-link)
(global-set-key "\C-cc" 'org-capture)
(global-set-key "\C-ca" 'org-agenda)
(global-set-key "\C-cb" 'org-iswitchb)

;; Calendar setting
;(require 'calendar)		      ; it's built-in.
;(calendar-set-date-style 'iso)	      ; set the "year/month/day" style

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
