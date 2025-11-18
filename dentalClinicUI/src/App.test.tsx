import { render, screen } from '@testing-library/react';
import DentalClinicUI from './App';

test('renders brand name somewhere', () => {
    render(<DentalClinicUI />);
    // your default lang is 'ar', so look for Arabic brand or tweak to 'en' before asserting
    // Example: check for the “DC” logo box role or header buttons
    expect(screen.getByText('DC')).toBeInTheDocument();
});

import userEvent from '@testing-library/user-event';

test('switches language to English and updates UI', async () => {
    render(<DentalClinicUI />);
    // Open the language switcher
    const globeButton = screen.getByTitle(/اللغة|Language|भाषा/i);
    await userEvent.click(globeButton);

    // Wait for the English button to appear
    const englishButton = await screen.findByRole('button', { name: /EN/i });
    await userEvent.click(englishButton);

    // Check for English brand name or navigation
    const brandElements = await screen.findAllByText('DentalClinic');
    expect(brandElements.length).toBeGreaterThan(0);
    expect(screen.getByText('Home')).toBeInTheDocument();
});
