import type { InputHTMLAttributes, TextareaHTMLAttributes } from 'react';
import styles from './Input.module.scss';

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
}

interface TextAreaProps extends TextareaHTMLAttributes<HTMLTextAreaElement> {
  label?: string;
  error?: string;
}

export function Input({ label, error, className = '', id, ...rest }: InputProps) {
  return (
    <div className={styles.wrapper}>
      {label && <label className={styles.label} htmlFor={id}>{label}</label>}
      <input
        id={id}
        className={[styles.input, error ? styles['input--error'] : '', className].filter(Boolean).join(' ')}
        {...rest}
      />
      {error && <span className={styles.error}>{error}</span>}
    </div>
  );
}

export function TextArea({ label, error, className = '', id, ...rest }: TextAreaProps) {
  return (
    <div className={styles.wrapper}>
      {label && <label className={styles.label} htmlFor={id}>{label}</label>}
      <textarea
        id={id}
        className={[styles.input, error ? styles['input--error'] : '', className].filter(Boolean).join(' ')}
        {...rest}
      />
      {error && <span className={styles.error}>{error}</span>}
    </div>
  );
}
